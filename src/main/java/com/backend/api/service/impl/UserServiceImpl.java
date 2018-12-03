package com.backend.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.backend.api.repository.PasswordResetRepository;
import com.backend.api.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.api.dto.ResponseObject;
import com.backend.api.dto.UserForm;
import com.backend.api.model.PasswordReset;
import com.backend.api.model.User;
import com.backend.api.model.Zone;
import com.backend.api.service.SendMailService;
import com.backend.api.service.UserService;
import com.backend.api.service.ZoneService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.AmwayConstants;
import com.backend.api.util.AmwayStatuses;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordResetRepository resetRepository;

	@Autowired
	private ZoneService zonaService;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SendMailService sendMail;

	@Override
	public ResponseObject saveUser(UserForm userForm, Long imcNumber) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			List<AmwayCodes> validationErrors = new ArrayList<>();
			Zone ubicacion = null;
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_VALIDACION_FORMULARIO);

			if (!Util.isEmailValid(userForm.getEmail()))
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_EMAIL);

			if (!AmwayConstants.TITULAR.equals(userForm.getRol())
					&& !AmwayConstants.COTITULAR.equals(userForm.getRol()))
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_ROL);

			if (Util.isEmpty(userForm.getNombres()))
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_NOMBRES);

			if (Util.isEmpty(userForm.getUbicacion()))
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_UBICACION);
			else {
				ubicacion = zonaService.findZoneById(userForm.getUbicacion());
				if (ubicacion == null)
					validationErrors.add(AmwayCodes.ERROR_ZONA_DESCONOCIDA);
			}

			if (Util.isEmpty(userForm.getPassword()) || userForm.getPassword().length() < 5)
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_PASSWORD);

			String encodedPass = encoder.encode(userForm.getPassword());
			userForm.setPassword(encodedPass);

			Long fechaNacimientoTime = Util.getTimestamp(userForm.getFechaNacimiento());
			if (fechaNacimientoTime == null)
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_FECHA_NACIMIENTO);

			if (validationErrors.isEmpty()) {
				User user = new User();
				user.setEmail(userForm.getEmail());
				user.setFechaCreacion(System.currentTimeMillis());
				user.setFechaNacimiento(fechaNacimientoTime);
				user.setImcNumber(imcNumber);
				user.setNombres(userForm.getNombres());
				user.setPassword(encodedPass);
				user.setRol(userForm.getRol());
				user.setUbicacion(new ObjectId(userForm.getUbicacion()));

				// TODO: Cambiar el estado del registro del usuario cuando se cree la
				// confirmación por correo
				user.setEstado(AmwayStatuses.ACTIVE.name());

				userRepository.save(user);
				responseBuilder.setAmwayCode(AmwayCodes.CREACION_USUARIO_CORRECTA);
				responseBuilder.addPayloadProperty("user", user);
			} else {
				responseBuilder.addPayloadProperty("resquest", userForm);
				responseBuilder.addPayloadProperty("result", validationErrors);
			}
		} catch (DuplicateKeyException dke) {
			responseBuilder.setDetailedMessage(dke.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_USUARIO_DUPLICADO);
			responseBuilder.addPayloadProperty("resquest", userForm);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(dke));
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_CREACION_USUARIO);
			responseBuilder.addPayloadProperty("resquest", userForm);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}

	@Override
	public ResponseObject restorePassword(Long imcNumber, String rol) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_ENVIANDO_EMAIL);
			if (Util.isEmpty(rol) || (!AmwayConstants.TITULAR.equals(rol) && !AmwayConstants.COTITULAR.equals(rol)))
				responseBuilder.setAmwayCode(AmwayCodes.ERROR_VALIDACION_ROL);
			else {
				User user = userRepository.findUserByIMCNumberRol(imcNumber, rol);
				if (user == null)
					responseBuilder.setAmwayCode(AmwayCodes.ERROR_USUARIO_NO_EXISTE);
				else {
					// Validar si ya lo había pedido
					PasswordReset password = resetRepository.findByUserState(new ObjectId(user.getId()),
							AmwayStatuses.ACTIVE.name());

					if (password != null)
						responseBuilder.setAmwayCode(AmwayCodes.ERROR_YA_EXISTE_PASS_RESET);
					else {
						PasswordReset passwordReset = new PasswordReset();
						passwordReset.setEstado(AmwayStatuses.ACTIVE.name());
						passwordReset.setUsuario(new ObjectId(user.getId()));
						passwordReset.setPassword(Util.getRandomPassword());

						resetRepository.save(passwordReset);

						sendMail.sendResetPasswordMail(user, passwordReset);
						responseBuilder.setAmwayCode(AmwayCodes.CORREO_ENVIADO_CORRECTAMENTE);
					}
				}
			}
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_ENVIANDO_EMAIL);
			responseBuilder.addPayloadProperty("resquest", "imc: " + imcNumber + ", rol: " + rol);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}

		return responseBuilder.build();
	}

	@Override
	public ResponseObject updateUserPassword(UserForm userForm, Long imcNumber, String rol) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			List<AmwayCodes> validationErrors = new ArrayList<>();
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_VALIDACION_FORMULARIO);

			if (Util.isEmpty(userForm.getPassword()) || userForm.getPassword().length() < 5)
				validationErrors.add(AmwayCodes.ERROR_VALIDACION_PASSWORD);

			User user = userRepository.findUserByIMCNumberRol(imcNumber, rol);
			if (user == null)
				validationErrors.add(AmwayCodes.ERROR_USUARIO_NO_EXISTE);

			if (validationErrors.isEmpty()) {
				PasswordReset passwordReset = resetRepository.findByUserState(new ObjectId(user.getId()),
						AmwayStatuses.ACTIVE.name());
				if (passwordReset != null) {
					passwordReset.setEstado(AmwayStatuses.INACTIVE.name());
					passwordReset.setFechaReinicio(System.currentTimeMillis());
					resetRepository.save(passwordReset);
				}

				String encodedPass = encoder.encode(userForm.getPassword());
				userForm.setPassword(encodedPass);
				user.setPassword(encodedPass);
				user.setEstado(AmwayStatuses.ACTIVE.name());
				userRepository.save(user);
				responseBuilder.setAmwayCode(AmwayCodes.REINICIO_PASSWORD_CORRECTO);
				responseBuilder.addPayloadProperty("user", user);
			} else {
				responseBuilder.addPayloadProperty("resquest", userForm);
				responseBuilder.addPayloadProperty("result", validationErrors);
			}
		} catch (DuplicateKeyException dke) {
			responseBuilder.setDetailedMessage(dke.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_USUARIO_DUPLICADO);
			responseBuilder.addPayloadProperty("resquest", userForm);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(dke));
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_CREACION_USUARIO);
			responseBuilder.addPayloadProperty("resquest", userForm);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}
}

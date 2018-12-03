package com.backend.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.backend.api.repository.GoalRepository;
import com.backend.api.repository.GoalTypeRepository;
import com.backend.api.repository.UserGoalTypeRepository;
import com.backend.api.repository.UserRepository;
import com.backend.api.repository.mongoNative.MongoNativeQuery;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.backend.api.dto.GoalDTO;
import com.backend.api.dto.ResponseObject;
import com.backend.api.dto.UserGoalTypeDTO;
import com.backend.api.model.Goal;
import com.backend.api.model.GoalType;
import com.backend.api.model.User;
import com.backend.api.model.UserGoalType;
import com.backend.api.service.GoalService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.AmwayConstants;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;

@Service(value = "goalService")
public class GoalServiceImpl implements GoalService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GoalTypeRepository goalTypeRepository;

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private UserGoalTypeRepository userGoalTypeRepository;

	@Autowired
	private MongoNativeQuery mongoNativeQuery;

	@Override
	public ResponseObject listGoalsType(Long imcNumber, String rol) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_VALIDACION_PETICION);
			List<AmwayCodes> validationErrors = new ArrayList<>();
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_ENVIANDO_EMAIL);
			if (Util.isEmpty(rol) || (!AmwayConstants.TITULAR.equals(rol) && !AmwayConstants.COTITULAR.equals(rol)))
				responseBuilder.setAmwayCode(AmwayCodes.ERROR_VALIDACION_ROL);
			else {
				User user = userRepository.findUserByIMCNumberRol(imcNumber, rol);
				if (user == null) {
					// El usuario no existe
					validationErrors.add(AmwayCodes.ERROR_USUARIO_NO_EXISTE);
				} else {
					responseBuilder.setAmwayCode(AmwayCodes.PETICION_PROCESADA_CORRECTAMENTE);
					List<UserGoalTypeDTO> userGoalTypesResponse = new ArrayList<>();

					int periodo = Util.getPeriodo() - 1;
					// Obtener todos los tipos de metas del sistema
					List<GoalType> listGoalType = goalTypeRepository.findAllGoalTypes();
					HashMap<String, GoalType> mapGoalType = new HashMap<>();
					listGoalType.stream().forEach(g -> mapGoalType.put(g.getNombre(), g));

					// se valida si el usuario tiene tipo de metas
					List<UserGoalType> userGoalTypes = userGoalTypeRepository
							.findUserGoalTypesByUserPeriod(new ObjectId(user.getId()), periodo);

					if (Util.isEmpty(userGoalTypes)) {

						Integer lastPeriod = mongoNativeQuery.findMaxPeriodByUser(new ObjectId(user.getId()));
						System.out.println(lastPeriod);

						listGoalType.stream().forEach(g -> userGoalTypesResponse.add(new UserGoalTypeDTO(g.getNombre(),
								g.getI18n().get(LocaleContextHolder.getLocale().getLanguage()), 0)));
						responseBuilder.addPayloadProperty("showWizard", true);
						responseBuilder.addPayloadProperty("tipos", userGoalTypesResponse);
					} else {
						listGoalType.stream().filter(g -> {
							UserGoalType ugt = new UserGoalType();
							ugt.setTipoMeta(g.getNombre());
							ugt.setUsuario(new ObjectId(user.getId()));
							ugt.setPeriodo(periodo);
							return !userGoalTypes.contains(ugt);
						}).forEach(g -> userGoalTypesResponse.add(new UserGoalTypeDTO(g.getNombre(),
								g.getI18n().get(LocaleContextHolder.getLocale().getLanguage()), 0)));

						userGoalTypes.stream()
								.forEach(
										g -> userGoalTypesResponse
												.add(new UserGoalTypeDTO(g.getTipoMeta(),
														mapGoalType.get(g.getTipoMeta()).getI18n()
																.get(LocaleContextHolder.getLocale().getLanguage()),
														g.getTotal())));
					}
					responseBuilder.addPayloadProperty("tipos", userGoalTypesResponse);
				}
			}

		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_PROCESANDO_PETICION);
			responseBuilder.addPayloadProperty("resquest", null);// zoneLevel);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}

	/**
	 * @NOTA Médoto encargado de realizar validación de cada uno de los datos de
	 *       entrada para guardar una meta
	 */
	@Override
	public ResponseObject validateInfo(String tipometa, String idusuariotipometa, GoalDTO goalDTO) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			if (Util.isEmpty(tipometa))
				responseBuilder.setAmwayCode(AmwayCodes.ERROR_NO_ENVIO_TIPOMETA);
			else if (Util.isEmpty(idusuariotipometa))
				responseBuilder.setAmwayCode(AmwayCodes.ERROR_NO_ENVIO_USUARIOTIPOMETA);
			else if (goalDTO.getAvance() == null)
				responseBuilder.setAmwayCode(AmwayCodes.ERROR_NO_ENVIO_AVANCEMETA);
			else
				responseBuilder.setAmwayCode(AmwayCodes.PETICION_PROCESADA_CORRECTAMENTE);
				responseBuilder.setDetailedMessage(AmwayConstants.CONTINUE);
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_PROCESANDO_PETICION);
			responseBuilder.addPayloadProperty("resquest", "Ejecución de Meta");
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}

	/**
	 * @NOTA Método encagado de realizar el insert de la meta
	 */
	@Override
	public ResponseObject insertGoal(String tipometa, String idusuariotipometa, GoalDTO goalDTO) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			Goal goal = new Goal();
			goal.setTipometa(tipometa);
			goal.setId(new ObjectId(idusuariotipometa.toString()));
			goal.setNombre(goalDTO.getNombre());
			goal.setAvance(goalDTO.getAvance());
			goal.setFecha(System.currentTimeMillis());
			goal.setDescripcion(goalDTO.getDescripcion());
			goalRepository.save(goal);
			responseBuilder.setAmwayCode(AmwayCodes.CREACION_META_CORRECTA);
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_PROCESANDO_PETICION);
			responseBuilder.addPayloadProperty("resquest", null);// zoneLevel);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}
}

package com.backend.api.service.impl;

import java.util.Arrays;
import java.util.List;

import com.backend.api.repository.PasswordResetRepository;
import com.backend.api.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;

import com.backend.api.dto.AuthUser;
import com.backend.api.dto.ResponseObject;
import com.backend.api.model.PasswordReset;
import com.backend.api.model.User;
import com.backend.api.service.PromoterService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.AmwayConstants;
import com.backend.api.util.AmwayStatuses;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;

@Service(value = "authService")
public class AuthenticationService implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PromoterService imcService;

	@Autowired
	private PasswordResetRepository resetRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private MessageSource messageSource;

	public List<SimpleGrantedAuthority> getUserAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority(AmwayConstants.ROLE_USER));
	}

	public List<SimpleGrantedAuthority> getAdminAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority(AmwayConstants.ROLE_USER),
				new SimpleGrantedAuthority(AmwayConstants.ROLE_ADMIN));
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userId = authentication.getName();
		String password = authentication.getCredentials().toString();

		if (Util.isEmpty(userId) || (userId.charAt(0) != 'T' && userId.charAt(0) != 'C'))
			throw new OAuth2Exception("Formato de usuario invalido");

		Long imcNumber = null;
		try {
			imcNumber = Long.parseLong(userId.substring(1));
			ResponseObject imcResponse = imcService.validateIMC(imcNumber);
			if (AmwayCodes.CODIGO_IMC_NO_EXISTE.value() == imcResponse.getCode()
					|| AmwayCodes.CODIGO_IMC_NO_VALIDO.value() == imcResponse.getCode())
				throw new OAuth2Exception("Código IMC no válido");
		} catch (Exception e) {
			throw new OAuth2Exception("Formato de usuario invalido");
		}

		User user = userRepository.findUserByIMCNumberRol(imcNumber,
				userId.charAt(0) == 'T' ? AmwayConstants.TITULAR : AmwayConstants.COTITULAR);

		if (user == null)
			throw new OAuth2Exception("Credenciales inválidas.");

		if (!AmwayStatuses.ACTIVE.name().equals(user.getEstado())
				&& !AmwayStatuses.PASSWORD_RESTART.name().equals(user.getEstado()))
			throw new OAuth2Exception("Credenciales inválidas.");

		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		responseBuilder.addPayloadProperty("user", user);

		PasswordReset passwordReset = resetRepository.findByUserState(new ObjectId(user.getId()),
				AmwayStatuses.ACTIVE.name());

		if (encoder.matches(password, user.getPassword())) {
			if (passwordReset != null) {
				passwordReset.setEstado(AmwayStatuses.INACTIVE.name());
				passwordReset.setFechaPeticion(System.currentTimeMillis());
				resetRepository.save(passwordReset);
			}
			responseBuilder.setAmwayCode(AmwayCodes.PETICION_PROCESADA_CORRECTAMENTE);

			return new UsernamePasswordAuthenticationToken(
					new AuthUser(userId, user.getPassword(), getUserAuthority(), responseBuilder.build()), password,
					getUserAuthority());

		} else if (passwordReset != null && password.equals(passwordReset.getPassword())) {
			user.setEstado(AmwayStatuses.PASSWORD_RESTART.name());
			userRepository.save(user);
			responseBuilder.setAmwayCode(AmwayCodes.REINICIO_PASS_NECESARIO);

			return new UsernamePasswordAuthenticationToken(
					new AuthUser(userId, user.getPassword(), getUserAuthority(), responseBuilder.build()), password,
					getUserAuthority());
		}

		throw new OAuth2Exception("Invalid credentials");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
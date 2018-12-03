package com.backend.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.dto.ResponseObject;
import com.backend.api.dto.UserForm;
import com.backend.api.service.PromoterService;
import com.backend.api.service.UserService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PromoterService imcService;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/{imcNumber}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResponseObject> createUser(@PathVariable(value = "imcNumber") Long imcNumber,
			@RequestBody UserForm userForm) {
		ResponseObject imcResponse = imcService.validateIMC(imcNumber);
		if (AmwayCodes.CODIGO_IMC_NO_EXISTE.value() == imcResponse.getCode()
				|| AmwayCodes.CODIGO_IMC_NO_VALIDO.value() == imcResponse.getCode())
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(imcResponse);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.saveUser(userForm, imcNumber));
	}

	@RequestMapping(value = "password/{imcNumber}/{rol}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResponseObject> updateUserPassword(@PathVariable(value = "imcNumber") Long imcNumber,
			@PathVariable(value = "rol") String rol, @RequestBody UserForm userForm, HttpServletRequest request) {
		ResponseObject imcResponse = imcService.validateIMC(imcNumber);
		if (AmwayCodes.CODIGO_IMC_NO_EXISTE.value() == imcResponse.getCode()
				|| AmwayCodes.CODIGO_IMC_NO_VALIDO.value() == imcResponse.getCode())
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(imcResponse);
		ResponseObject res = userService.updateUserPassword(userForm, imcNumber, rol);
		if (res.getCode() == AmwayCodes.REINICIO_PASSWORD_CORRECTO.value())
			revokeToken(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
	}

	@RequestMapping(value = "/{imcNumber}/{rol}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseObject> restorePassword(@PathVariable(value = "imcNumber") Long imcNumber,
			@PathVariable(value = "rol") String rol) {
		ResponseObject imcResponse = imcService.validateIMC(imcNumber);
		if (AmwayCodes.CODIGO_IMC_NO_EXISTE.value() == imcResponse.getCode()
				|| AmwayCodes.CODIGO_IMC_NO_VALIDO.value() == imcResponse.getCode())
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(imcResponse);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.restorePassword(imcNumber, rol));
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseObject> logoutPage(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(revokeToken(request));
	}

	private ResponseObject revokeToken(HttpServletRequest request) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			String token = request.getHeader(HttpHeaders.AUTHORIZATION);
			token = token.substring(7);
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
			tokenStore.removeAccessToken(accessToken);
			responseBuilder.setAmwayCode(AmwayCodes.PETICION_PROCESADA_CORRECTAMENTE);
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_PROCESANDO_PETICION);
			responseBuilder.addPayloadProperty("resquest", "logout");
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}
}

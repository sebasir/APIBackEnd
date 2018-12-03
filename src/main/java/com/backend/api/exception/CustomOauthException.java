package com.backend.api.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {

	private static final long serialVersionUID = 5973944085124853915L;

	public CustomOauthException(String msg, Throwable t) {
		super(msg, t);
	}
}
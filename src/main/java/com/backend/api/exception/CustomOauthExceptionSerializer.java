package com.backend.api.exception;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;

import com.backend.api.util.AmwayCodes;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthException> {

	private static final long serialVersionUID = -3171634632887510477L;

	@Autowired
	private MessageSource messageSource;

	public CustomOauthExceptionSerializer() {
		super(CustomOauthException.class);
	}

	@Override
	public void serialize(CustomOauthException value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		ResponseObjectBuilder builder = new ResponseObjectBuilder(messageSource);
		if (value.getCause() instanceof InvalidGrantException)
			builder.setAmwayCode(AmwayCodes.ERROR_CREDENCIALES_INVALIDAS);
		else
			builder.setAmwayCode(AmwayCodes.ERROR_USUARIO_NO_AUTORIZADO);
		builder.setDetailedMessage(value.getMessage());
		builder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(value));
		gen.writeObject(builder.build());
	}
}
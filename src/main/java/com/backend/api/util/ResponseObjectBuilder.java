package com.backend.api.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.backend.api.dto.ResponseObject;

public class ResponseObjectBuilder {

	private MessageSource messageSource;
	private Map<String, Object> payload;
	private AmwayCodes amwayCode;
	private String detailedMessage;
	
	public ResponseObjectBuilder(MessageSource messageSource) {
		this.payload = new HashMap<>();
		this.messageSource = messageSource;
	}

	public void addPayloadProperty(String field, Object value) {
		payload.put(field, value);
	}

	public void setAmwayCode(AmwayCodes amwayCode) {
		this.amwayCode = amwayCode;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

	public ResponseObject build() {
		ResponseObject responseObject = new ResponseObject();
		responseObject.setDetailedMessage(detailedMessage);
		responseObject.setPayload(payload);
		responseObject.setMessage(messageSource.getMessage(amwayCode.name(), null, LocaleContextHolder.getLocale()));
		responseObject.setCode(amwayCode.value());
		return responseObject;
	}

}

package com.backend.api.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.backend.api.dto.AuthUser;
import com.backend.api.dto.ResponseObject;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		AuthUser authUser = (AuthUser) authentication.getPrincipal();
		final Map<String, Object> additionalInfo = new HashMap<>();
		ResponseObject user = authUser.getResponseObject();
		additionalInfo.put("message", user.getMessage());
		additionalInfo.put("detailedMessage", user.getDetailedMessage());
		additionalInfo.put("code", user.getCode());
		additionalInfo.put("payload", user.getPayload());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

		return accessToken;
	}

}
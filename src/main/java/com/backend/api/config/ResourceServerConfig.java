package com.backend.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.backend.api.util.AmwayConstants;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private BasicAuthenticationEntryPoint basicAuthenticationPoint;

	private static final String RESOURCE_ID = "resource_id";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.anonymous().disable().authorizeRequests().antMatchers("/goal/{\\d+}", "/users/password/**").access(AmwayConstants.HAS_ROLE_USER)
				.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler()).and().httpBasic()
				.authenticationEntryPoint(basicAuthenticationPoint).and().authorizeRequests()
				//.antMatchers("/users/**", "/promoter/**", "/zone/**").authenticated();
				.antMatchers("/none/**").authenticated();
	}
}
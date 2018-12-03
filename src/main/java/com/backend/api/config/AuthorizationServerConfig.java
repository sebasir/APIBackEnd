package com.backend.api.config;

import java.util.Arrays;

import com.backend.api.exception.CustomOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
@PropertySource("classpath:application.properties")
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	public enum Props {
		CLIENT_ID("auth.config.client-id"), CLIENT_SECRET("auth.config.client-secret"),
		GRANT_TYPE("auth.config.grant_type"), SCOPE_READ("auth.config.read_scope"),
		SCOPE_WRITE("auth.config.write_scope"),
		ACCESS_TOKEN_VALIDITY_SECONDS("auth.config.access_token_validity_seconds"),
		REFRESH_TOKEN_VALIDITY_SECONDS("auth.config.refresh_token_validity_seconds");

		public String value;

		private Props(String value) {
			this.value = value;
		}
	}

	@Autowired
	private Environment env;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.inMemory().withClient(env.getProperty(Props.CLIENT_ID.value))
				.secret(env.getProperty(Props.CLIENT_SECRET.value))
				.authorizedGrantTypes(env.getProperty(Props.GRANT_TYPE.value))
				.scopes(env.getProperty(Props.SCOPE_READ.value), env.getProperty(Props.SCOPE_WRITE.value))
				.accessTokenValiditySeconds(
						Integer.parseInt(env.getProperty(Props.ACCESS_TOKEN_VALIDITY_SECONDS.value)))
				.refreshTokenValiditySeconds(
						Integer.parseInt(env.getProperty(Props.REFRESH_TOKEN_VALIDITY_SECONDS.value)));
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
		endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
				.authenticationManager(authenticationManager).exceptionTranslator(e -> {
					if (e instanceof OAuth2Exception) {
						OAuth2Exception oAuth2Exception = (OAuth2Exception) e;

						return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								.body(new CustomOauthException(oAuth2Exception.getMessage(), oAuth2Exception));
					}
					throw e;
				}).tokenEnhancer(tokenEnhancerChain);
	}
}
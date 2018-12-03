package com.backend.api.config;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.backend.api.util.AmwayConstants;
import com.backend.api.util.Util;

public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {

	public SmartLocaleResolver(Locale locale) {
		super();
		super.setDefaultLocale(locale);
	}

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		if (Util.isEmpty(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)))
			return AmwayConstants.DEFAULT_LOCALE;

		List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
		Locale locale = Locale.lookup(list, AmwayConstants.LOCALES);
		return locale;
	}
}
package com.backend.api.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AmwayConstants {
	public static final String APP_DEFAULT_ENCODING = "UTF-8";
	public static final String TITULAR = "TITULAR";
	public static final String COTITULAR = "COTITULAR";
	public static final String FORMATO_FECHA = "dd/MM/yyyy";
	public static final String ZONA_NIVEL_PAIS = "PAIS";
	public static final String ZONA_NIVEL_PROV = "PROV";
	public static final String ZONA_NIVEL_CIUDAD = "CIUDAD";
	public static final String ADMIN = "ADMIN";
	public static final String USER = "USER";
	public static final String ANONYMOUS = "ANONYMOUS";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
	public static final String HAS_ROLE_USER = "hasRole('USER')";
	public static final String HAS_ROLE_ANONYMOUS = "hasRole('ANONYMOUS')";
	public static final List<Locale> LOCALES = Collections
			.unmodifiableList(Arrays.asList(new Locale("es"), new Locale("en"), new Locale("pt")));
	public static final Locale DEFAULT_LOCALE = LOCALES.get(0);
	public static final String RESET_PASS_TEMPLATE = "templates/pass_reset.html";
	public static final String AMWAY_LOGO_IMAGE = "templates/images/logo_amway.png";
	public static final String CONTINUE="Continue";
}

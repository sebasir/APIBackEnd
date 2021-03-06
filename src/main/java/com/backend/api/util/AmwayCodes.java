package com.backend.api.util;

public enum AmwayCodes {
	// Peticiones correctas
	PETICION_PROCESADA_CORRECTAMENTE(1000), CODIGO_IMC_VALIDO(1001), CODIGO_IMC_VALIDO_SIN_REGISTRO(1002),
	CODIGO_IMC_VALIDO_SIN_TITULAR(1003), CODIGO_IMC_VALIDO_SIN_COTITULAR(1004), CODIGO_IMC_NO_EXISTE(1005),
	CODIGO_IMC_NO_VALIDO(1006), CORREO_ENVIADO_CORRECTAMENTE(1007), REINICIO_PASS_NECESARIO(1008),

	// Operaciones CRUD correctas
	CREACION_USUARIO_CORRECTA(1501), REINICIO_PASSWORD_CORRECTO(1502),CREACION_META_CORRECTA(1503),

	// Operaciones sobre formularios fallidas
	ERROR_VALIDACION_PETICION(2001), ERROR_VALIDACION_FORMULARIO(2002), ERROR_VALIDACION_EMAIL(2003),
	ERROR_VALIDACION_ROL(2004), ERROR_VALIDACION_NOMBRES(2005), ERROR_VALIDACION_UBICACION(2006),
	ERROR_VALIDACION_FECHA_NACIMIENTO(2007), ERROR_VALIDACION_PASSWORD(2008), ERROR_CREACION_USUARIO(2009),
	ERROR_USUARIO_DUPLICADO(2010), ERROR_ZONA_DESCONOCIDA(2011), ERROR_CREDENCIALES_INVALIDAS(2012),
	ERROR_FORMATO_CREDENCIALES(2013), ERROR_USUARIO_BLOQUEADO(2014), ERROR_USUARIO_INACTIVO(2015),
	ERROR_USUARIO_PENDIENTE_CONFIRMAR(2016), ERROR_USUARIO_REINICIO_PASSWORD(2017), ERROR_USUARIO_NO_EXISTE(2018),
	ERROR_ENVIANDO_EMAIL(2019), ERROR_USUARIO_NO_AUTORIZADO(2020), ERROR_YA_EXISTE_PASS_RESET(2021),
	
	ERROR_NO_ENVIO_TIPOMETA(2022),ERROR_NO_ENVIO_USUARIOTIPOMETA(2023),ERROR_NO_ENVIO_NOMBREMETA(2024),
	ERROR_NO_ENVIO_AVANCEMETA(2025),ERROR_NO_ENVIO_DESCRIPCIONMETA(2026),CONTINUE(2026),
	// Error de peticiones
	ERROR_PROCESANDO_PETICION(5001),

	// Codigos para plantillas HTML
	HTML_PASS_RESET_HELLO_AGAIN(0), HTML_PASS_RESET_PASSWORD_RESET(0), HTML_PASS_RESET_NEXT_STEPS(0),
	HTML_PASS_RESET_NOT_YOU(0), HTML_PASS_RESET_SOMEONE_TRYING(0), HTML_PASS_RESET_REQUEST_DETAILS(0),
	HTML_PASS_RESET_REQUEST_DATETIME(0), HTML_PASS_RESET_IP_ADDRESS(0), HTML_PASS_RESET_IMC_NUMBER(0),
	HTML_PASS_RESET_ROL(0), EMAIL_ASUNTO_PASS_RESET(0);
	
	

	private final int value;

	AmwayCodes(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
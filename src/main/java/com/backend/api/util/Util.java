package com.backend.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class Util {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AmwayConstants.FORMATO_FECHA);

	public static boolean isEmailValid(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static boolean isEmpty(List<?> lista) {
		return lista == null || lista.isEmpty();
	}

	public static Long getTimestamp(String stringDate) {
		return Timestamp.valueOf(LocalDate.parse(stringDate, formatter).atStartOfDay()).getTime();
	}

	public static int getPeriodo() {
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Z"));
		return date.getYear() * 100 + date.getMonthValue();
	}

	public static String getRandomPassword() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(uuid.lastIndexOf("-") + 1);
	}

	public static void main(String... args) {
		Arrays.asList(AmwayCodes.values()).stream().forEach(c -> System.out.println(c.name() + ": " + c.value()));

	}

//		Map<String, String> r = new HashMap<>();
//		r.put("#helloAgain", "Hola de nuevo, ");
//		r.put("#userNames", "Omar Andrés Agudelo Giraldo");
//		r.put("#passwordReset",
//				"Has solicitado reinicio de tu contraseña, y para completarlo, necesitamos que la próxima vez que ingreses utilices la siguiente contraseña");
//		r.put("#nextSteps", "La aplicación te informará de los pasos a seguir.");
//		r.put("#notYou", "No fuiste tú?");
//		r.put("#someoneTrying",
//				"Al parecer alguien más tiene tu código de afiliación y rol que es necesario para reiniciar la contraseña. No pasará nada más, amenos que hayas olvidado tu contraseña");
//		r.put("#requestDetails", "Detalles de la solicitud de reinicio");
//		r.put("#IMCNumber", "Código IMC");
//		r.put("#requestRole", "Rol");
//		r.put("#requestDateTime", "Fecha y hora de la solicitud");
//		r.put("#requestIpAddress", "Dirección IP");
//
//		r.put("#ipAddressValue", "192.168.0.1");
//		r.put("#dateTimeValue", "01/01/1900 12:34:56");
//		r.put("#roleValue", "COTITULAR");
//		r.put("#imcNumberValue", "1900121231");
//		r.put("#newPass", "124c12345ctw34ftwde");
//		String path = "C:\\Users\\Admin\\Workspace_\\amway-api\\src\\main\\resources\\templates\\pass_reset.html";
//		try {
//			System.out.println(createHTMLMessageBody(r, path));
//		} catch (Exception e) {
//			System.out.println("Error procesando mensaje: " + e.getMessage());
//		}
//	}

	/**
	 * @author Sebastian Motavita
	 * @Company Periferia it
	 * @text Método para ejecución dinamica de mensajes al properties
	 * @date 06/11/2018
	 */
	public static String getMessage(MessageSource messageSource, String code, String[] params) {
		return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}

	/**
	 * @END ######################## getMessage
	 *      ########################################
	 */

	/**
	 * @author Sebastian Motavita
	 * @Company Periferia it
	 * @text Método para creación dinamica del HTML por medio de plantilla
	 * @date 06/11/2018
	 */
	public static String createHTMLMessageBody(Map<String, String> replacement, String templatePath) throws Exception {
		StringBuilder body = new StringBuilder();
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(Util.class.getClassLoader().getResourceAsStream(templatePath)))) {
			String line = null;
			while ((line = in.readLine()) != null)
				body.append(line);

			replacement.entrySet().stream().forEach(s -> {
				int ini = body.indexOf(s.getKey());
				body.replace(ini, ini + s.getKey().length(), s.getValue());
			});
		}
		return body.toString();
	}

	/**
	 * @END ######################## createHTMLMessageBody
	 *      ########################################
	 */

	public static String getStackTraceMessage(Throwable e) {
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
			e.printStackTrace(pw);
			return Arrays.asList(sw.toString().split("\n")).stream().limit(10).collect(Collectors.joining("\n"));
		} catch (Exception ex) {
			return e.getMessage();
		}
	}

	public static String formatTimestamp(long timestamp) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Z")).toString();
	}
}

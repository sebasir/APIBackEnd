package com.backend.api.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.backend.api.model.PasswordReset;
import com.backend.api.model.User;
import com.backend.api.service.SendMailService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.AmwayConstants;
import com.backend.api.util.Util;

/**
 * 
 * @author Omar Agudelo
 * @Company Periferia it
 * @text Clase para envío de correos
 * @date 06/11/2018
 *
 */
@Service
public class SendMailServiceImpl implements SendMailService {

	/**
	 * 
	 * @author Omar Agudelo
	 * @Company Periferia it
	 * @text Método para realizar la ejecución del priperties mensajes por medio del
	 *       enum ejecución
	 * @date 06/11/2018
	 *
	 */
	private enum Props {
	MAIL_PROTOCOL("spring.mail.protocol"), MAIL_HOST("spring.mail.host"), MAIL_PORT("spring.mail.port"),
	MAIL_USER("spring.mail.username"), MAIL_PWD("spring.mail.password"),
	MAIL_AUTH("spring.mail.properties.mail.smtp.auth"),
	MAIL_STARTTLS("spring.mail.properties.mail.smtp.starttls.enable"),
	MAIL_SOCKETFACTORY("spring.mail.smtp.socketFactory.class"),
	MAIL_FALLBACK("spring.mail.smtp.socketFactory.fallback");

		private String value;

		private Props(String value) {
			this.value = value;
		}
	}

	/**
	 * @END ######################## Props ########################################
	 */
	@Autowired
	private Environment env;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 
	 * @author Omar Agudelo
	 * @Company Periferia it
	 * @text Método para realizar el envío del correo
	 * @date 06/11/2018
	 *
	 */
	private void sendMail(String recipient, String subject, String htmlMessage) {
		String host = env.getProperty(Props.MAIL_HOST.value);
		String protocol = env.getProperty(Props.MAIL_PROTOCOL.value);
		String userName = env.getProperty(Props.MAIL_USER.value);
		String password = env.getProperty(Props.MAIL_PWD.value);
		String port = env.getProperty(Props.MAIL_PORT.value);
		String auth = env.getProperty(Props.MAIL_AUTH.value);
		String socketFactoryClass = env.getProperty(Props.MAIL_SOCKETFACTORY.value);
		String starttls = env.getProperty(Props.MAIL_STARTTLS.value);

		try {
			java.util.Properties props = null;
			props = System.getProperties();
			props.put("mail.smtp.user", userName);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.debug", "true");
			if (!Util.isEmpty(port)) {
				props.put("mail.smtp.port", port);
				props.put("mail.smtp.socketFactory.port", port);
			}

			if (!Util.isEmpty(starttls))
				props.put("mail.smtp.starttls.enable", starttls);

			if (!Util.isEmpty(socketFactoryClass))
				props.put("mail.smtp.socketFactory.class", socketFactoryClass);

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});
			session.setDebug(true);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(userName));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			msg.setSubject(subject, AmwayConstants.APP_DEFAULT_ENCODING);
			msg.setSentDate(new Date());

			MimeMultipart multiPart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setHeader("content-Type",
					"text/html;charset=\"" + AmwayConstants.APP_DEFAULT_ENCODING + "\"");
			messageBodyPart.setContent(htmlMessage,
					"text/html;charset=\"" + AmwayConstants.APP_DEFAULT_ENCODING + "\"");
			multiPart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();

			DataSource dataSource = new DataSource() {
				@Override
				public String getContentType() {
					return "image/png";
				}

				@Override
				public InputStream getInputStream() {
					return Util.class.getClassLoader().getResourceAsStream(AmwayConstants.AMWAY_LOGO_IMAGE);
				}

				@Override
				public String getName() {
					return "logoAmway.png";
				}

				@Override
				public OutputStream getOutputStream() {
					throw new RuntimeException("unreachable");
				}
			};

			messageBodyPart.setDataHandler(new DataHandler(dataSource));
			messageBodyPart.setHeader("Content-ID", "<logo>");

			multiPart.addBodyPart(messageBodyPart);

			msg.setContent(multiPart);
			msg.saveChanges();

			Transport transport = session.getTransport(protocol);
			transport.connect(host, userName, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}

	/**
	 * @END ######################## sendMail
	 *      ########################################
	 */

	/**
	 * 
	 * @author Omar Agudelo
	 * @Company Periferia it
	 * @text Método para realizar la ejecución del llamado del sericio
	 * @date 06/11/2018
	 *
	 */
	@Override
	public void sendResetPasswordMail(User user, PasswordReset passwordReset) throws Exception {
		Map<String, String> r = new HashMap<>();
		r.put("#helloAgain", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_HELLO_AGAIN.name(), null));
		r.put("#userNames", user.getNombres());
		r.put("#passwordReset", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_PASSWORD_RESET.name(), null));
		r.put("#nextSteps", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_NEXT_STEPS.name(), null));
		r.put("#notYou", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_NOT_YOU.name(), null));
		r.put("#someoneTrying", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_SOMEONE_TRYING.name(), null));
		r.put("#requestDetails",
				Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_REQUEST_DETAILS.name(), null));
		r.put("#IMCNumber", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_IMC_NUMBER.name(), null));
		r.put("#requestRole", Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_ROL.name(), null));
		r.put("#requestDateTime",
				Util.getMessage(messageSource, AmwayCodes.HTML_PASS_RESET_REQUEST_DATETIME.name(), null));
		// r.put("#requestIpAddress", Util.getMessage(messageSource,
		// AmwayCodes.HTML_PASS_RESET_IP_ADDRESS.name(), null));
		r.put("#dateTimeValue", Util.formatTimestamp(System.currentTimeMillis()));
		r.put("#roleValue", user.getRol());
		r.put("#imcNumberValue", user.getImcNumber().toString());
		r.put("#newPass", passwordReset.getPassword());
		sendMail(user.getEmail(),
				messageSource.getMessage(AmwayCodes.EMAIL_ASUNTO_PASS_RESET.name(), null,
						LocaleContextHolder.getLocale()),
				Util.createHTMLMessageBody(r, AmwayConstants.RESET_PASS_TEMPLATE));
	}
	/**
	 * @END ######################## sendResetPasswordMail
	 *      ########################################
	 */
}

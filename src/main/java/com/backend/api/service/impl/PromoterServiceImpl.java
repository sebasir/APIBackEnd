package com.backend.api.service.impl;

import java.util.List;
import java.util.Optional;

import com.backend.api.repository.PromoterRepository;
import com.backend.api.repository.UserRepository;
import com.backend.api.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.backend.api.dto.ResponseObject;
import com.backend.api.model.Promoter;
import com.backend.api.model.User;
import com.backend.api.model.Zone;
import com.backend.api.service.PromoterService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.AmwayConstants;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;

@Service(value = "promoterService")
public class PromoterServiceImpl implements PromoterService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PromoterRepository promoterRepository;

	@Autowired
	private ZoneRepository zoneRepository;

	@Autowired
	private MessageSource messageSource;

	@Override
	public ResponseObject validateIMC(Long imcNumber) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			if (imcNumber != null && imcNumber.toString().length() == 10) {
				long countryCode = imcNumber / 10000000;
				Zone zona = zoneRepository.findZoneByCode(countryCode);
				if (zona != null) {
					Promoter emp = promoterRepository.findPromoterByIMCNumber(imcNumber);
					if (emp != null && emp.getImcNumber() != null) {
						List<User> users = userRepository.findUsersByIMCNumber(imcNumber);
						Optional<User> titular = users.stream().filter(u -> AmwayConstants.TITULAR.equals(u.getRol()))
								.findFirst();
						Optional<User> cotitular = users.stream()
								.filter(u -> AmwayConstants.COTITULAR.equals(u.getRol())).findFirst();

						if (titular.isPresent() && cotitular.isPresent())
							responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_VALIDO);
						else if (titular.isPresent() && !cotitular.isPresent())
							responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_VALIDO_SIN_COTITULAR);
						else if (!titular.isPresent() && cotitular.isPresent())
							responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_VALIDO_SIN_TITULAR);
						else
							responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_VALIDO_SIN_REGISTRO);

						responseBuilder.addPayloadProperty("pais", zona);
					} else
						responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_NO_EXISTE);
				} else
					responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_NO_VALIDO);
			} else
				responseBuilder.setAmwayCode(AmwayCodes.CODIGO_IMC_NO_VALIDO);
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_PROCESANDO_PETICION);
			responseBuilder.addPayloadProperty("resquest", imcNumber);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}
}

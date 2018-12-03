package com.backend.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.backend.api.repository.ZoneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.backend.api.dto.ResponseObject;
import com.backend.api.model.Zone;
import com.backend.api.service.ZoneService;
import com.backend.api.util.AmwayCodes;
import com.backend.api.util.AmwayConstants;
import com.backend.api.util.ResponseObjectBuilder;
import com.backend.api.util.Util;

@Service(value = "zoneService")
public class ZoneServiceImpl implements ZoneService {

	@Autowired
	private ZoneRepository zoneRepository;

	@Override
	public Zone findZoneById(String zoneId) {
		return zoneRepository.findZoneById(zoneId);
	}

	@Autowired
	private MessageSource messageSource;

	@Override
	public ResponseObject listByLevel(String zoneLevel, String parentZone) {
		ResponseObjectBuilder responseBuilder = new ResponseObjectBuilder(messageSource);
		try {
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_VALIDACION_PETICION);
			List<AmwayCodes> validationErrors = new ArrayList<>();
			List<Zone> zones = null;
			if (Util.isEmpty(zoneLevel)
					|| (!AmwayConstants.ZONA_NIVEL_PAIS.equals(zoneLevel) && Util.isEmpty(parentZone)))
				validationErrors.add(AmwayCodes.ERROR_ZONA_DESCONOCIDA);
			else {
				zones = zoneRepository.findZonesByLevelParent(zoneLevel,
						parentZone != null ? new ObjectId(parentZone) : null);
				if (zones.isEmpty())
					validationErrors.add(AmwayCodes.ERROR_ZONA_DESCONOCIDA);
			}

			if (validationErrors.isEmpty()) {
				responseBuilder.setAmwayCode(AmwayCodes.PETICION_PROCESADA_CORRECTAMENTE);
				responseBuilder.addPayloadProperty("zonas", zones);
			} else {
				responseBuilder.addPayloadProperty("resquest", "nivel: " + zoneLevel + ", padre: " + parentZone);
				responseBuilder.addPayloadProperty("result", validationErrors);
			}
		} catch (Exception e) {
			responseBuilder.setDetailedMessage(e.getMessage());
			responseBuilder.setAmwayCode(AmwayCodes.ERROR_PROCESANDO_PETICION);
			responseBuilder.addPayloadProperty("resquest", zoneLevel);
			responseBuilder.addPayloadProperty("stackTrace", Util.getStackTraceMessage(e));
		}
		return responseBuilder.build();
	}
}

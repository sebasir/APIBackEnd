package com.backend.api.service;

import com.backend.api.dto.ResponseObject;
import com.backend.api.model.Zone;

public interface ZoneService {
	ResponseObject listByLevel(String zoneLevel, String parentZone);

	Zone findZoneById(String zoneId);
}

package com.backend.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.dto.ResponseObject;
import com.backend.api.service.ZoneService;

@RestController
@RequestMapping("/zone")
public class ZoneController {

	@Autowired
	private ZoneService zoneService;

	@RequestMapping(value = "/list/{zoneLevel}/{parentZone}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseObject> listZonesByLevel(@PathVariable(value = "zoneLevel") String zoneLevel,
			@PathVariable(value = "parentZone") String parentZone) {
		return ResponseEntity.ok().body(zoneService.listByLevel(zoneLevel, parentZone));
	}

	@RequestMapping(value = "/list/{zoneLevel}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseObject> validateIMC(@PathVariable(value = "zoneLevel") String zoneLevel) {
		return ResponseEntity.ok().body(zoneService.listByLevel(zoneLevel, null));
	}
}

package com.backend.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.dto.ResponseObject;
import com.backend.api.service.PromoterService;

@RestController
@RequestMapping("/promoter")
public class PromoterController {

	@Autowired
	private PromoterService promoterService;

	@RequestMapping(value = "/validateIMC/{imcNumber}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseObject> validateIMC(@PathVariable(value = "imcNumber") Long imcNumber) {
		return ResponseEntity.ok().body(promoterService.validateIMC(imcNumber));
	}
}

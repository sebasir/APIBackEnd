package com.backend.api.service;

import com.backend.api.dto.ResponseObject;

public interface PromoterService {
	ResponseObject validateIMC(Long imcNumber);
}

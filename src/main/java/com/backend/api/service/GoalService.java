package com.backend.api.service;

import com.backend.api.dto.GoalDTO;
import com.backend.api.dto.ResponseObject;

public interface GoalService {
	ResponseObject listGoalsType(Long imcNumber, String rol);

	ResponseObject validateInfo(String tipometa, String idusuariotipometa,GoalDTO goalDTO);

	ResponseObject insertGoal(String tipometa, String idusuariotipometa, GoalDTO goalDTO);

}

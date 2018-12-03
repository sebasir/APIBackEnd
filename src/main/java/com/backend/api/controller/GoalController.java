package com.backend.api.controller;

import com.backend.api.dto.GoalDTO;
import com.backend.api.dto.ResponseObject;
import com.backend.api.service.GoalService;
import com.backend.api.service.PromoterService;
import com.backend.api.util.AmwayCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goal")
public class GoalController {

    @Autowired
    private PromoterService imcService;

    @Autowired
    private GoalService goalService;

    @RequestMapping(value = "/{imcNumber}/{rol}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ResponseObject> listGoals(@PathVariable(value = "imcNumber") Long imcNumber,
                                                    @PathVariable(value = "rol") String rol) {
        ResponseObject imcResponse = imcService.validateIMC(imcNumber);
        if (AmwayCodes.CODIGO_IMC_NO_EXISTE.value() == imcResponse.getCode()
                || AmwayCodes.CODIGO_IMC_NO_VALIDO.value() == imcResponse.getCode())
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(imcResponse);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(goalService.listGoalsType(imcNumber, rol));
    }

    @RequestMapping(value = "/{tipometa}/{idusuariotipometa}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<ResponseObject> Goal(@PathVariable(value = "tipometa") String tipometa,
                                               @PathVariable(value = "idusuariotipometa") String idusuariotipometa, @RequestBody GoalDTO goalDTO) {
        ResponseObject insertGoal = goalService.validateInfo(tipometa, idusuariotipometa, goalDTO);
        if (insertGoal.getCode() == AmwayCodes.PETICION_PROCESADA_CORRECTAMENTE.value())
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(goalService.insertGoal(tipometa, idusuariotipometa,
                    goalDTO));
        else
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(insertGoal);
    }
}

package com.rudyn.drones.controllers;

import com.rudyn.drones.errors.ValidationErrorResponse;
import com.rudyn.drones.model.Drone;
import com.rudyn.drones.model.Medication;
import com.rudyn.drones.service.DroneService;
import com.rudyn.drones.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("medication")
public class ApiMedication {

    @Autowired
    private DroneService droneService;
    @Autowired
    private MedicationService medicationService;

    @PostMapping("/save")
    public ResponseEntity<?> saveMedication(@Valid @RequestBody Medication m, BindingResult result){

        if(result.hasErrors()){
            List<String> fields = new ArrayList<>();
            List<String> errores = new ArrayList<>();

            for (FieldError error: result.getFieldErrors()){
                errores.add(error.getDefaultMessage());
                fields.add(error.getField());
            }
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(fields, errores);

            return ResponseEntity.badRequest().body(errorResponse);
        }
        return new ResponseEntity<Medication>(medicationService.saveMedication(m), HttpStatus.OK);
    }





}

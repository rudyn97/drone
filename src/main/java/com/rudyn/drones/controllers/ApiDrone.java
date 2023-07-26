package com.rudyn.drones.controllers;

import com.rudyn.drones.errors.ValidationErrorResponse;
import com.rudyn.drones.errors.ValidationOnlyErrorResponse;
import com.rudyn.drones.model.Drone;
import com.rudyn.drones.model.Medication;
import com.rudyn.drones.service.DroneService;
import com.rudyn.drones.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("drone")
public class ApiDrone {

    @Autowired
    private DroneService droneService;
    @Autowired
    private MedicationService medicationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDrone(@Valid @RequestBody Drone d, BindingResult result){

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
        d.setState("IDLE");
        return new ResponseEntity<Drone>(droneService.saveDrone(d), HttpStatus.OK);
    }
    

    @PostMapping("/loading/{id}")
    public ResponseEntity<?> loadingDrone(@Valid @RequestBody Medication med, BindingResult result, @PathVariable String id){
        ValidationOnlyErrorResponse errorResponse = new ValidationOnlyErrorResponse();

        if (!droneService.getDroneById(id).isPresent()){
            errorResponse.setErrors(List.of("Verify that the drone is in the database"));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        double med_weight=0;
        if (!medicationService.getMedicationById(med.getCode()).isPresent()){
            medicationService.saveMedication(med);
        }

            if (medicationService.getMedicationById(med.getCode()).isPresent()){
                Drone d = droneService.getDroneById(id).get();

                //Prevent drone low battery
                if (d.getBattery_capacity()<25){
                    errorResponse.setErrors(List.of("The battery needs to recharge to continue filling"));
                    return ResponseEntity.badRequest().body(errorResponse);
                }
                //Prevent drone more weight
                double total_weight = 0;
                double limit = Double.parseDouble(d.getWeight_limit().toString());
                med_weight += Double.parseDouble(med.getWeight().toString());
                if (total_weight+med_weight<limit){
                    if (d.getMedications() == null){ d.setMedications(List.of(med));
                    }else {d.getMedications().add(med);}

                    if (med.getDrones() == null){ med.setDrones(List.of(d));
                    }else {med.getDrones().add(d);}
                    d.setState("LOADING");
                }
                if (total_weight+med_weight==limit){
                    if (d.getMedications() == null){ d.setMedications(List.of(med));
                    }else {d.getMedications().add(med);}

                    if (med.getDrones() == null){ med.setDrones(List.of(d));
                    }else {med.getDrones().add(d);}
                    d.setState("LOADED");
                }
                if (total_weight+med_weight>limit){
                    errorResponse.setErrors(List.of("This drone cannot carry more weight"));
                    return ResponseEntity.badRequest().body(errorResponse);
                }

                medicationService.saveMedication(med);
                return new ResponseEntity<Drone>(droneService.saveDrone(d), HttpStatus.OK);

            }else{
                errorResponse.setErrors(List.of("These medication were not found: "));
                return ResponseEntity.badRequest().body(errorResponse);
            }
    }

    @PutMapping("/loading_medications")
    public ResponseEntity<?> loadingDroneMedications( @RequestParam  String serial, @RequestParam List<String> code){
        List<String> errores = new ArrayList<>();
        ValidationOnlyErrorResponse errorResponse = new ValidationOnlyErrorResponse();

        if (!droneService.getDroneById(serial).isPresent()){
            errorResponse.setErrors(List.of("Verify that the drone is in the database"));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        errores.add("These medications were not found: ");
        boolean err = false;
        double med_weight=0;

        double total_weight = 0;

        for (int i = 0; i < code.size(); i++) {
            if (medicationService.getMedicationById(code.get(i)).isPresent()){

                Drone d = droneService.getDroneById(serial).get();
                Medication med = medicationService.getMedicationById(code.get(i)).get();
                //Prevent drone low battery
                if (d.getBattery_capacity()<25){
                    errorResponse.setErrors(List.of("The battery needs to recharge to continue filling"));
                    return ResponseEntity.badRequest().body(errorResponse);
                }
                //Prevent drone more weight
                double limit = Double.parseDouble(d.getWeight_limit().toString());
                med_weight += Double.parseDouble(med.getWeight().toString());
                if (total_weight+med_weight<limit){
                    if (d.getMedications() == null){ d.setMedications(List.of(med));
                    }else {d.getMedications().add(med);}

                    if (med.getDrones() == null){ med.setDrones(List.of(d));
                    }else {med.getDrones().add(d);}
                    d.setState("LOADING");
                }
                if (total_weight+med_weight==limit){
                    if (d.getMedications() == null){ d.setMedications(List.of(med));
                    }else {d.getMedications().add(med);}

                    if (med.getDrones() == null){ med.setDrones(List.of(d));
                    }else {med.getDrones().add(d);}
                    d.setState("LOADED");
                }
                if (total_weight+med_weight>limit){
                    errorResponse.setErrors(List.of("This drone cannot carry more weight"));
                    return ResponseEntity.badRequest().body(errorResponse);
                }

//                medicationService.saveMedication(med);
                return new ResponseEntity<Drone>(droneService.saveDrone(d), HttpStatus.OK);

            }else{
                err = true;
                errores.add(code.get(i)+", ");
            }
        }
        if (err) {
            errorResponse.setErrors(errores);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return null;
    }


    @GetMapping("/check_medicine/{id}")
    public ArrayList<Medication> getDroneMedicineById(@PathVariable("id") String id){
        ArrayList<Medication> m = new ArrayList<>();
        if (droneService.getDroneById(id).isPresent()){
            Drone d = droneService.getDroneById(id).get();
            m.addAll(d.getMedications());
        }
        return m;
    }

    @GetMapping("/available_loading")
    public ArrayList<Drone> getAvailableLoading(){
        List<Drone> drones = droneService.getAllDrone();
        ArrayList<Drone> drones_al = new ArrayList<>();
        for (int i = 0; i < drones.size(); i++) {
            if (drones.get(i).getBattery_capacity()>=25){
                drones_al.add(drones.get(i));
            }
        }
        return drones_al;
    }

    @GetMapping("/check_battery/{id}")
    public String getDroneBatteryById(@PathVariable("id") String id){
        try{
            return "Battery level is: "+droneService.getDroneById(id).get().getBattery_capacity();
        }catch (Exception e){
            return "An error has ocurred \n"+e;
        }
    }


}

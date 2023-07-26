package com.rudyn.drones.service;

import com.rudyn.drones.model.Medication;

import java.util.ArrayList;
import java.util.Optional;

public interface MedicationService {

    ArrayList<Medication> getAllMedication();
    Optional<Medication> getMedicationById(String id);
    Medication saveMedication(Medication u);
    boolean deleteMedicationById(String id);

}

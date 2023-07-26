package com.rudyn.drones.service;

import com.rudyn.drones.model.Medication;
import com.rudyn.drones.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MedicationServiceImpl implements MedicationService{

    @Autowired
    MedicationRepository medicationRepository;

    @Override
    public ArrayList<Medication> getAllMedication() {
        return (ArrayList<Medication>) medicationRepository.findAll();
    }

    @Override
    public Optional<Medication> getMedicationById(String id) {
        return medicationRepository.findById(id);
    }

    @Override
    public Medication saveMedication(Medication u) {
        return medicationRepository.save(u);
    }

    @Override
    public boolean deleteMedicationById(String id) {
        try {
            Optional<Medication> m = getMedicationById(id);
            medicationRepository.delete(m.get());
            return true;
        }catch (Exception e){
            return false;
        }

    }
}

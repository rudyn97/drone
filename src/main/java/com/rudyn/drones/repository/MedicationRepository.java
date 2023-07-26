package com.rudyn.drones.repository;

import com.rudyn.drones.model.Medication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicationRepository extends CrudRepository<Medication, String> {
}

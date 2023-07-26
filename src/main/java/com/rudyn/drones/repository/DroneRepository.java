package com.rudyn.drones.repository;

import com.rudyn.drones.model.Drone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DroneRepository extends CrudRepository<Drone, String> {
}

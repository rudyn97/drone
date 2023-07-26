package com.rudyn.drones.service;

import com.rudyn.drones.model.Drone;

import java.util.ArrayList;
import java.util.Optional;

public interface DroneService {

    ArrayList<Drone> getAllDrone();
    Optional<Drone> getDroneById(String serial_number);
    Drone saveDrone(Drone u);
    boolean deleteDroneById(String serial_number);
    void getDroneBattery();

}

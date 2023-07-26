package com.rudyn.drones.service;

import com.rudyn.drones.model.Drone;
import com.rudyn.drones.repository.DroneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
@EnableScheduling
public class DroneServiceImpl implements DroneService{

    @Autowired
    DroneRepository droneRepository;

    @Override
    public ArrayList<Drone> getAllDrone() {
        return (ArrayList<Drone>) droneRepository.findAll();
    }

    @Override
    public Optional<Drone> getDroneById(String serial_number) {
        return droneRepository.findById(serial_number);
    }

    @Override
    public Drone saveDrone(Drone u) {
        return droneRepository.save(u);
    }

    @Override
    public boolean deleteDroneById(String serial_number) {
        try {
            Optional<Drone> d = getDroneById(serial_number);
            droneRepository.delete(d.get());
            return true;
        }catch (Exception e){
            return false;
        }
    }
    static final Logger LOGGER = Logger.getLogger("Log");

    @Scheduled(fixedDelay = 60000)
    public void getDroneBattery(){
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
        String timeHour = dateTimeFormat.format(LocalDateTime.now());
        List<Drone> d = getAllDrone();

        try {
            FileHandler fileHandler = new FileHandler("./data/drone_battery.log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            LOGGER.info(timeHour);

            for (int i = 0; i < d.size(); i++) {
                LOGGER.info("Serial number: "+d.get(i).getSerial_number()+" / Battery capacity: "
                        +d.get(i).getBattery_capacity());
            }
            fileHandler.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

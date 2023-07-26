package com.rudyn.drones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
public class Drone {


    @Id
    @NotEmpty(message = "The serial number must not be empty")
    @NotNull(message = "The serial number must not be empty")
    @Size(max = 100, message = "The serial number must have a maximum of 100 characters")
    private String serial_number;

    @NotEmpty(message = "The model must not be empty")
    @NotNull(message = "The model must not be empty")
    @Pattern(regexp = "(?i)Lightweight|Middleweight|Cruiserweight|Heavyweight",
    message = "The model must contain Lightweight, Middleweight, Cruiserweight or Heavyweight")
    private String model;

    @NotNull(message = "The weight limit must not be empty")
    @DecimalMax(value = "500", message = "The weight limit must be 500 grams maximum")
    private BigDecimal weight_limit;

    @NotNull(message = "The battery capacity must not be empty")
    @Max(value = 100, message = "The battery capacity must be 100% maximum")
    @Min(value = 0, message = "The battery capacity must be 0% minimum")
    @Digits(message = "The battery capacity must be numeric", integer = 10, fraction = 0)
    private int battery_capacity;

//    @NotEmpty(message = "The state must not be empty")
//    @NotNull(message = "The state must not be empty")
//    @Pattern(regexp = "IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING",
//            message = "The state must contain IDLE, LOADING, LOADED, DELIVERING, DELIVERED or RETURNING")
    private String state;

    @ManyToMany(targetEntity = Medication.class, cascade = CascadeType.ALL)
    private List<Medication> medications;

    public Drone() {
    }

    public Drone(String serial_number, String model, BigDecimal weight_limit, int battery_capacity, String state) {
        this.serial_number = serial_number;
        this.model = model;
        this.weight_limit = weight_limit;
        this.battery_capacity = battery_capacity;
        this.state = state;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getWeight_limit() {
        return weight_limit;
    }

    public void setWeight_limit(BigDecimal weight_limit) {
        this.weight_limit = weight_limit;
    }

    public int getBattery_capacity() {
        return battery_capacity;
    }

    public void setBattery_capacity(int battery_capacity) {
        this.battery_capacity = battery_capacity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}

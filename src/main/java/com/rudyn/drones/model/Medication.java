package com.rudyn.drones.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
public class Medication {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;

    @NotEmpty(message = "The name must not be empty")
    @NotNull(message = "The name must not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "The name must contain letters, numbers, - or _")
    private String name;

    @NotNull(message = "The weight must not be empty")
    @Digits(message = "The weight must be numeric", integer = 10, fraction = 2)
    private BigDecimal weight;

    @Id
    @NotEmpty(message = "The code must not be empty")
    @NotNull(message = "The code must not be empty")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "The code must contain uppercase letters, numbers or _")
    private String code;

    @NotEmpty(message = "The image must not be empty")
    @NotNull(message = "The image must not be empty")
    private String image;


    @ManyToMany(targetEntity = Drone.class, mappedBy = "medications", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Drone> drones;


    public Medication() {
    }

    public Medication(String name, BigDecimal weight, String code, String image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Drone> getDrones() {
        return drones;
    }

    public void setDrones(List<Drone> drones) {
        this.drones = drones;
    }
}

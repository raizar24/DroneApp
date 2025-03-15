package com.drone.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
public class Drone {
    @Id
    @Size(max = 100) //Serial Number (100 characters max)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel droneModel; //(Lightweight, Middleweight, Cruiserweight, Heavyweight)

    @Min(0)
    @Max(1000)
    private int weightLimit;

    @Min(0)
    @Max(100)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState droneState; //(IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING)

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    private Set<DeliveryList> deliveries;

    //default constructor
    public Drone(){};

    public Drone(String serialNumber, DroneModel droneModel, int weightLimit,
                 int batteryCapacity, DroneState droneState) {
        this.serialNumber = serialNumber;
        this.droneModel = droneModel;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.droneState = droneState;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getDroneModel() {
        return droneModel;
    }

    public void setDroneModel(DroneModel droneModel) {
        this.droneModel = droneModel;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public DroneState getDroneState() {
        return droneState;
    }

    public void setDroneState(DroneState droneState) {
        this.droneState = droneState;
    }

    // full description
    @Override
    public String toString() {
        return "Drone{" +
                "serialNumber='" + serialNumber + '\'' +
                ", model=" + droneModel +
                ", weightLimit=" + weightLimit +
                ", batteryCapacity=" + batteryCapacity + "%" +
                ", state=" + droneState +
                '}';
    }
}

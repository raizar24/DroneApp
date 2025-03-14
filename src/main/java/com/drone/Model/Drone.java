package com.drone.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Drone {

    @Id
    @Size(max = 100)
    private String serialNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DroneModel droneModel;

    @NotNull
    @Min(0)
    @Max(1000)
    private int weightLimit;

    @NotNull
    @Min(0)
    @Max(100)
    private int batteryCapacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DroneState droneState;

    //for default constructor
    public Drone() {}

    // Parameterized constructor
    public Drone(String serialNumber, DroneModel droneModel, int weightLimit, int batteryCapacity, DroneState droneState) {
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

    public DroneModel getModel() {
        return droneModel;
    }

    public void setModel(DroneModel droneModel) {
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

    public DroneState getState() {
        return droneState;
    }

    public void setState(DroneState droneState) {
        this.droneState = droneState;
    }

    // full description
    @Override
    public String toString() {
        return "Drone{" +
                "serialNumber='" + serialNumber + '\'' +
                ", model=" + droneModel +
                ", weightLimit=" + weightLimit +
                ", batteryCapacity=" + batteryCapacity +
                ", state=" + droneState +
                '}';
    }

}
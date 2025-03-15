package com.drone.Model;

import jakarta.persistence.*;

@Entity
public class DeliveryList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drone_serial_number", nullable = false)
    private Drone drone;

    @ManyToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    private int quantity; // Quantity of the medication in the delivery

    // Default constructor
    public DeliveryList() {}

    public DeliveryList(Drone drone, Medication medication, int quantity) {
        this.drone = drone;
        this.medication = medication;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate total weight for this medication in the delivery
    public int getTotalWeight() {
        return medication.getWeight() * quantity;
    }

    @Override
    public String toString() {
        return "DeliveryList{" +
                "id=" + id +
                ", drone=" + drone.getSerialNumber() +
                ", medication=" + medication.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
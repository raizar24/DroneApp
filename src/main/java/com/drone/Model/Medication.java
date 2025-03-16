package com.drone.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$",message = "Invalid medication name") //(allowed only letters, numbers, -, _)
    private String name;

    @NotNull
    private int weight;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9_-]+$",message = "Invalid medication code") //(allowed only uppercase letters, underscores and numbers)
    private String code;

    private String image;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL)
    private Set<DeliveryList> deliveries;
    //default
    public Medication() {}

    public Medication(String name, int weight, String code, String image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
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

    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", code='" + code + '\'' +
                ", image=" + (image != null ? "exists" : "not present") +
                '}';
    }

}

package com.drone.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Medication {

    @Id
    @NotNull
    private int id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$") //only allows "a-z","A-Z","0-9","-","_" characters
    private String name;

    @NotNull
    private int weight;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9_-]+$") //only allows,"A-Z","0-9","-","_" characters
    private String code;

    @Lob
    private byte[] image;

    //default
    public Medication() {}

    public Medication(String name, int weight, String code, byte[] image) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    // full description
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

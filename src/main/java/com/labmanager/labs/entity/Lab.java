package com.labmanager.labs.entity;

import jakarta.persistence.*;

/*
 * CREATE TABLE laboratory (
  lab_id VARCHAR(255) NOT NULL,
  location VARCHAR(255) NOT NULL,
  PRIMARY KEY (lab_id)
    );
 */

@Entity
@Table(name = "laboratory")
public class Lab {

    @Id  // primary key
    @Column(name = "lab_id", nullable = false)
    private String labId;

    @Column(name = "location", nullable = false)
    private String location;

    private String occupactionType;

    public Lab() {}

    public Lab(String labId, String location) {
        this.labId = labId;
        this.location = location;
        this.occupactionType = "Available";
    }

    public String getLabId() {
        return labId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupactionType() {
        return occupactionType;
    }

    public void setOccupactionType(String occupactionType) {
        this.occupactionType = occupactionType;
    }
}

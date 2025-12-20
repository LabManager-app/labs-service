package com.labmanager.labs.entity;

import jakarta.persistence.*;

/*
  CREATE TABLE equipment (
  lab_id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  stock INTEGER NOT NULL,
  current_usage INTEGER NOT NULL,
  PRIMARY KEY (lab_id, name),
  FOREIGN KEY (lab_id) REFERENCES laboratory(lab_id)
    );
 */

@Entity
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lab_id", nullable = false)
    private String labId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private int stock;

    @Column(name = "current_usage", nullable = false)
    private int currentUsage;

    public Equipment() {}

    public Equipment(String labId, String name, int stock, int currentUsage) {
        this.labId = labId;
        this.name = name;
        this.stock = stock;
        this.currentUsage = currentUsage;
    }

    public Long getId() {
        return id;
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public int getCurrentUsage() {
        return currentUsage;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCurrentUsage(int currentUsage) {
        this.currentUsage = currentUsage;
    }
}

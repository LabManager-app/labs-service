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

    @EmbeddedId
    private EquipmentId id;

    /*
    @ManyToOne
    @MapsId("labId")
    @JoinColumn(name = "lab_id")
    private Lab lab;
    */

    @Column(nullable = false)
    private int stock;

    @Column(name = "current_usage", nullable = false)
    private int currentUsage;

    public Equipment() {}

    public Equipment(String labId, String name, int stock, int currentUsage) {
        this.id = new EquipmentId(labId, name);
        this.stock = stock;
        this.currentUsage = currentUsage;
    }

    public EquipmentId getId() {
        return id;
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

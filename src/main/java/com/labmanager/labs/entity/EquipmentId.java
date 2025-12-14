package com.labmanager.labs.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Embeddable
public class EquipmentId implements Serializable {

    @Column(name = "lab_id")
    private String labId;

    @Column(name = "name")
    private String name;

    public EquipmentId() {}

    public EquipmentId(String labId, String name) {
        this.labId = labId;
        this.name = name;
    }

    // equals & hashCode OBVEZNO!
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof EquipmentId)) return false;
        EquipmentId that = (EquipmentId) other;
        return labId.equals(that.labId) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return labId.hashCode() + name.hashCode();
    }
}

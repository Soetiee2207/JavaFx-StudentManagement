package com.example.btl_dacntt.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public abstract class Student {
    protected int id;
    protected String studentId;
    protected String fullNameStudent;
    protected LocalDate dateOfBirth;
    protected String classId;

    public abstract float calculateAverage(float cc, float gk, float ck);
    public abstract void displayInfo();

    public String getTypeString() {
        if (this instanceof RegularStudent) return "Regular";
        if (this instanceof InServiceStudent) return "InService";
        return "Unknown";
    }
}
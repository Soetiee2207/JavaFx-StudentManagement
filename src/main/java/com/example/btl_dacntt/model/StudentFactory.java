package com.example.btl_dacntt.model;

public class StudentFactory {
    public static Student createStudent(String type) {
        if (type == null) {
            return null;
        }
        if (type.equalsIgnoreCase("Regular")) {
            return new RegularStudent();
        }
        else if (type.equalsIgnoreCase("InService")) {
            return new InServiceStudent();
        }
        return null;
    }
}
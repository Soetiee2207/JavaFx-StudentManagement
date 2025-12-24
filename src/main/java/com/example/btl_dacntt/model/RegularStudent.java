package com.example.btl_dacntt.model;

public class RegularStudent extends Student {
    @Override
    public float calculateAverage(float cc, float gk, float ck) {
        return (float) (cc * 0.1 + gk * 0.3 + ck * 0.6);
    }
    public boolean checkExamCondition() {
        return true;
    }
    @Override
    public void displayInfo() {
        System.out.println("Sinh viên Chính quy: " + getFullNameStudent());
    }
}
package com.example.btl_dacntt.model;

public class InServiceStudent extends Student {

    @Override
    public float calculateAverage(float cc, float gk, float ck) {
        return (float) (cc * 0.15 + gk * 0.25 + ck * 0.6);
    }

    public boolean checkExamCondition() {
        return true;
    }
    @Override
    public void displayInfo() {
        System.out.println("Sinh viên Tại chức: " + getFullNameStudent());
    }
}
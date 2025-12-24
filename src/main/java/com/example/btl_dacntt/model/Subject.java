package com.example.btl_dacntt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    private String subjectId;
    private String subjectName;
    private int credits;

    @Override
    public String toString() {
        return subjectName;
    }
}
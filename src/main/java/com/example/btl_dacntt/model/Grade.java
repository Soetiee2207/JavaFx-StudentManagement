package com.example.btl_dacntt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    private int id;
    private String studentId;
    private String subjectId;
    private float scoreCc;
    private float scoreGk;
    private float scoreCk;
    private float scoreTk;

    private String studentType;
    private String studentName;
    private String subjectName;
}
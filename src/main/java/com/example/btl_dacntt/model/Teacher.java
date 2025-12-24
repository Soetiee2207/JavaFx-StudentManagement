package com.example.btl_dacntt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private String teacherId;
    private String fullName;
    private String email;

    private int roleId;
    private String username;
    private String password;
}
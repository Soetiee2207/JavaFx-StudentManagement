package com.example.btl_dacntt.dao;

import com.example.btl_dacntt.model.Student;
import com.example.btl_dacntt.model.StudentFactory;
import com.example.btl_dacntt.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String type = rs.getString("type");

                Student s = StudentFactory.createStudent(type);

                if (s != null) {
                    s.setId(rs.getInt("id"));
                    s.setStudentId(rs.getString("student_id"));
                    s.setFullNameStudent(rs.getString("full_name")); // Map fullNameStudent <-> full_name
                    s.setClassId(rs.getString("class_id"));

                    Date sqlDate = rs.getDate("dob");
                    if (sqlDate != null) {
                        s.setDateOfBirth(sqlDate.toLocalDate());
                    }

                    list.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addStudent(Student s) {
        String sql = "INSERT INTO students (student_id, full_name, dob, class_id, type) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, s.getStudentId());
            pstmt.setString(2, s.getFullNameStudent());

            if (s.getDateOfBirth() != null) {
                pstmt.setDate(3, Date.valueOf(s.getDateOfBirth()));
            } else {
                pstmt.setNull(3, Types.DATE);
            }

            pstmt.setString(4, s.getClassId());
            pstmt.setString(5, s.getTypeString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student s) {
        String sql = "UPDATE students SET full_name=?, dob=?, class_id=? WHERE student_id=?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, s.getFullNameStudent());

            if (s.getDateOfBirth() != null) {
                pstmt.setDate(2, Date.valueOf(s.getDateOfBirth()));
            } else {
                pstmt.setNull(2, Types.DATE);
            }

            pstmt.setString(3, s.getClassId());
            pstmt.setString(4, s.getStudentId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE student_id=?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
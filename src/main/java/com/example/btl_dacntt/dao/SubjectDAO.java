package com.example.btl_dacntt.dao;

import com.example.btl_dacntt.model.Subject;
import com.example.btl_dacntt.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public List<Subject> getAllSubjects() {
        List<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM subjects";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Subject s = new Subject(
                        rs.getString("subject_id"),
                        rs.getString("subject_name"),
                        rs.getInt("credits")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void addSubject(Subject s) {
        String sql = "INSERT INTO subjects (subject_id, subject_name, credits) VALUES (?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, s.getSubjectId());
            pstmt.setString(2, s.getSubjectName());
            pstmt.setInt(3, s.getCredits());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateSubject(Subject s) {
        String sql = "UPDATE subjects SET subject_name=?, credits=? WHERE subject_id=?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, s.getSubjectName());
            pstmt.setInt(2, s.getCredits());
            pstmt.setString(3, s.getSubjectId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteSubject(String subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id=?";
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, subjectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
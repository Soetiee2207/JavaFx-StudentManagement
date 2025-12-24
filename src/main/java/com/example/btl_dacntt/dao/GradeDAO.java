package com.example.btl_dacntt.dao;

import com.example.btl_dacntt.model.Grade;
import com.example.btl_dacntt.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    public List<Grade> getAllGrades() {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT g.*, s.full_name, s.type, sub.subject_name " +
                "FROM grades g " +
                "JOIN students s ON g.student_id = s.student_id " +
                "JOIN subjects sub ON g.subject_id = sub.subject_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Grade g = new Grade();
                g.setId(rs.getInt("id"));
                g.setStudentId(rs.getString("student_id"));
                g.setSubjectId(rs.getString("subject_id"));
                g.setScoreCc(rs.getFloat("score_cc"));
                g.setScoreGk(rs.getFloat("score_gk"));
                g.setScoreCk(rs.getFloat("score_ck"));
                g.setScoreTk(rs.getFloat("score_tk"));

                g.setStudentName(rs.getString("full_name"));
                g.setStudentType(rs.getString("type"));
                g.setSubjectName(rs.getString("subject_name"));

                list.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
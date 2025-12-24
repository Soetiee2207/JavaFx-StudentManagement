package com.example.btl_dacntt.dao;

import com.example.btl_dacntt.model.Teacher;
import com.example.btl_dacntt.utils.DatabaseConnection;
import com.example.btl_dacntt.utils.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    public List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT t.teacher_id, t.full_name, t.email, u.role_id, u.username, u.password " +
                "FROM teachers t " +
                "JOIN users u ON u.teacher_id = t.teacher_id " +
                "WHERE u.role_id = 2";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Teacher(
                        rs.getString("teacher_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getInt("role_id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean addTeacher(Teacher t) {
        Connection conn = null;
        PreparedStatement pstmtTeacher = null;
        PreparedStatement pstmtUser = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Bước 1: Thêm giáo viên vào bảng teachers trước
            String sqlTeacher = "INSERT INTO teachers (teacher_id, full_name, email) VALUES (?, ?, ?)";
            pstmtTeacher = conn.prepareStatement(sqlTeacher);
            pstmtTeacher.setString(1, t.getTeacherId());
            pstmtTeacher.setString(2, t.getFullName());
            pstmtTeacher.setString(3, t.getEmail());
            int teacherResult = pstmtTeacher.executeUpdate();
            
            if (teacherResult <= 0) {
                conn.rollback();
                return false;
            }

            // Bước 2: Hash password và thêm vào bảng users
            String hashedPassword = PasswordUtils.hashPassword(t.getPassword());
            
            String sqlUser = "INSERT INTO users (username, password, role_id, teacher_id) VALUES (?, ?, ?, ?)";
            pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, t.getUsername());
            pstmtUser.setString(2, hashedPassword);
            pstmtUser.setInt(3, 2); // Role 2 = Teacher
            pstmtUser.setString(4, t.getTeacherId());
            int userResult = pstmtUser.executeUpdate();
            
            if (userResult <= 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try { if(pstmtUser != null) pstmtUser.close(); } catch (Exception e) {};
            try { if(pstmtTeacher != null) pstmtTeacher.close(); } catch (Exception e) {};
            try { if(conn != null) conn.close(); } catch (Exception e) {};
        }
    }
    public void updateTeacher(Teacher t) {
        Connection conn = null;
        PreparedStatement pstmtTeacher = null;
        PreparedStatement pstmtUser = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sqlTeacher = "UPDATE teachers SET full_name = ?, email = ? WHERE teacher_id = ?";
            pstmtTeacher = conn.prepareStatement(sqlTeacher);
            pstmtTeacher.setString(1, t.getFullName());
            pstmtTeacher.setString(2, t.getEmail());
            pstmtTeacher.setString(3, t.getTeacherId());
            pstmtTeacher.executeUpdate();

            String sqlUser = "UPDATE users SET username = ?, password = ? WHERE teacher_id = ?";
            pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, t.getUsername());
            pstmtUser.setString(2, t.getPassword());
            pstmtUser.setString(3, t.getTeacherId());
            pstmtUser.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if(pstmtUser != null) pstmtUser.close(); } catch (Exception e) {};
            try { if(pstmtTeacher != null) pstmtTeacher.close(); } catch (Exception e) {};
            try { if(conn != null) conn.close(); } catch (Exception e) {};
        }
    }
    public void deleteTeacher(String teacherId) {
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtTeacher = null;

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            String sqlUser = "DELETE FROM users WHERE teacher_id = ?";
            pstmtUser = conn.prepareStatement(sqlUser);
            pstmtUser.setString(1, teacherId);
            pstmtUser.executeUpdate();

            String sqlTeacher = "DELETE FROM teachers WHERE teacher_id = ?";
            pstmtTeacher = conn.prepareStatement(sqlTeacher);
            pstmtTeacher.setString(1, teacherId);
            int rowsAffected = pstmtTeacher.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                System.out.println("Đã xóa thành công giáo viên và tài khoản liên quan.");
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try { if(pstmtUser != null) pstmtUser.close(); } catch (Exception e) {};
            try { if(pstmtTeacher != null) pstmtTeacher.close(); } catch (Exception e) {};
            try { if(conn != null) conn.close(); } catch (Exception e) {};
        }
    }
}
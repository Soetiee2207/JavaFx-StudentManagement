package com.example.btl_dacntt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button btn_teacher;
    @FXML
    private Button btn_subject;
    @FXML
    private Label lblWelcome;
    public void setWelcomeMessage(String message) {
        lblWelcome.setText(message);
    }
    @FXML
    public void initialize() {
        showHome(null);
    }
    @FXML
    void showHome(ActionEvent event) {
        loadView("home-view.fxml");
//        mainBorderPane.setCenter(new Label("Trang chủ"));
    }
    @FXML
    void showStudents(ActionEvent event) {
        loadView("student-view.fxml");
//        mainBorderPane.setCenter(new Label("Đang hiển thị danh sách Sinh viên..."));
    }
    @FXML
    void showTeachers(ActionEvent event) {
        loadView("teacher-view.fxml");
    }
    @FXML
    void showSubjects(ActionEvent event) {
        loadView("subject-view.fxml");
//        mainBorderPane.setCenter(new Label("Đang hiển thị danh sách Môn học..."));
    }
    @FXML
    void showGrades(ActionEvent event) {
        loadView("grade-view.fxml");
//        mainBorderPane.setCenter(new Label("Đang hiển thị Bảng điểm..."));
    }
    @FXML
    void logout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) mainBorderPane.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/btl_dacntt/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Hệ Thống Quản Lý Sinh Viên - Đăng Nhập");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadView(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/btl_dacntt/" + fxmlName));
            mainBorderPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUserId(int id){
        if (id == 1) btn_teacher.setVisible(true);
        else {
            btn_teacher.setVisible(false);
            btn_teacher.setManaged(false);
            btn_subject.setVisible(false);
            btn_subject.setManaged(false);
        }
    }
}
package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.UserDAO;
import com.example.btl_dacntt.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMessage;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    protected void login(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Vui lòng điền đầy đủ thông tin!");
            lblMessage.setVisible(true);
            return;
        }

        User user = userDAO.checkLogin(username, password);

        if (user != null) {
            lblMessage.setText("Đăng nhập thành công!");
            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setVisible(true);

            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/btl_dacntt/dashboard-view.fxml"));
                Parent root = loader.load();

                DashboardController dashboardController = loader.getController();

                dashboardController.setUserId(user.getRoleId());
                String roleName = "";
                switch (user.getRoleId()) {
                    case 1: roleName = "Quản Trị Viên"; break;
                    case 2: roleName = "Giáo Viên"; break;
                }

                dashboardController.setWelcomeMessage("Xin chào: " + user.getUsername() + " (" + roleName + ")");

                Stage stage = new Stage();
                stage.setTitle("Hệ Thống Quản Lý Đào Tạo");
                stage.setScene(new Scene(root));
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                lblMessage.setText("Lỗi: Không tìm thấy file Dashboard!");
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        } else {
            lblMessage.setText("Sai tài khoản hoặc mật khẩu!");
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setVisible(true);
        }
    }

    @FXML
    protected void goToRegister(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/btl_dacntt/register-view.fxml"));
            Parent root = loader.load();

            currentStage.setScene(new Scene(root, 400, 600));
            currentStage.setTitle("Đăng Ký");
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("Lỗi: Không thể mở trang đăng ký!");
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setVisible(true);
        }
    }
}
package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.UserDAO;
import com.example.btl_dacntt.dao.TeacherDAO;
import com.example.btl_dacntt.model.Teacher;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private ComboBox<String> cboRole;
    @FXML
    private VBox teacherInfoSection;
    @FXML
    private TextField txtTeacherId;
    @FXML
    private TextField txtFullName;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblMessage;

    private final UserDAO userDAO = new UserDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();

    @FXML
    public void initialize() {
        cboRole.setItems(FXCollections.observableArrayList("Admin", "Giáo viên"));
        cboRole.getSelectionModel().selectFirst();
        
        cboRole.setOnAction(event -> {
            boolean isTeacher = "Giáo viên".equals(cboRole.getValue());
            teacherInfoSection.setVisible(isTeacher);
            teacherInfoSection.setManaged(isTeacher);
        });
    }

    @FXML
    protected void register(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String selectedRole = cboRole.getValue();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Vui lòng điền đầy đủ thông tin!", true);
            return;
        }

        if (username.length() < 4) {
            showMessage("Tên đăng nhập phải có ít nhất 4 ký tự!", true);
            return;
        }

        if (password.length() < 6) {
            showMessage("Mật khẩu phải có ít nhất 6 ký tự!", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Mật khẩu xác nhận không khớp!", true);
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            showMessage("Tên đăng nhập đã tồn tại!", true);
            return;
        }

        int roleId;
        String teacherId = null;

        if ("Giáo viên".equals(selectedRole)) {
            roleId = 2; // Teacher
            teacherId = txtTeacherId.getText().trim();
            String fullName = txtFullName.getText().trim();
            String email = txtEmail.getText().trim();

            if (teacherId.isEmpty() || fullName.isEmpty()) {
                showMessage("Vui lòng nhập đầy đủ Mã GV và Họ tên!", true);
                return;
            }

            // Tạo giáo viên trong bảng teachers trước, sau đó tạo user
            Teacher newTeacher = new Teacher(teacherId, fullName, email, roleId, username, password);
            boolean success = teacherDAO.addTeacher(newTeacher);
            
            if (success) {
                showMessage("Đăng ký giáo viên thành công! Đang chuyển về trang đăng nhập...", false);
                
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(() -> goToLogin(event));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showMessage("Đăng ký thất bại! Mã giáo viên hoặc username có thể đã tồn tại.", true);
            }
        } else {
            roleId = 1; // Admin
            boolean success = userDAO.createUser(username, password, roleId, teacherId);

            if (success) {
                showMessage("Đăng ký thành công! Đang chuyển về trang đăng nhập...", false);
                
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        javafx.application.Platform.runLater(() -> goToLogin(event));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showMessage("Đăng ký thất bại! Vui lòng thử lại.", true);
            }
        }
    }

    @FXML
    protected void goToLogin(ActionEvent event) {
        try {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/btl_dacntt/login-view.fxml"));
            Parent root = loader.load();

            currentStage.setScene(new Scene(root, 400, 500));
            currentStage.setTitle("Đăng Nhập");
            currentStage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Lỗi: Không thể chuyển về trang đăng nhập!", true);
        }
    }

    private void showMessage(String message, boolean isError) {
        lblMessage.setText(message);
        lblMessage.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        lblMessage.setVisible(true);
    }
}

package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.TeacherDAO;
import com.example.btl_dacntt.model.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.btl_dacntt.dao.UserDAO;

public class TeacherController {

    @FXML private TableView<Teacher> tableTeacher;
    @FXML private TableColumn<Teacher, String> colId;
    @FXML private TableColumn<Teacher, String> colName;
    @FXML private TableColumn<Teacher, String> colEmail;
    @FXML private TableColumn<Teacher, Integer> colRole;
    @FXML private TableColumn<Teacher, String> colUser;

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    
    @FXML private Label lblSelectedUser;
    @FXML private PasswordField txtNewPassword;

    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    
    private Teacher selectedTeacherForReset = null;
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleId"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));

        loadData();

        tableTeacher.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getTeacherId());
                txtName.setText(newSelection.getFullName());
                txtEmail.setText(newSelection.getEmail());
                txtUser.setText(newSelection.getUsername());
                txtPass.clear();

                txtId.setDisable(true);
                
                selectedTeacherForReset = newSelection;
                lblSelectedUser.setText("Đã chọn: " + newSelection.getUsername());
                lblSelectedUser.setStyle("-fx-font-weight: bold; -fx-text-fill: #8e44ad;");
            }
        });
    }

    private void loadData() {
        teacherList.clear();
        teacherList.addAll(teacherDAO.getAllTeachers());
        tableTeacher.setItems(teacherList);
    }

    @FXML
    private void addTeacher() {
        String id = txtId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String user = txtUser.getText();
        String pass = txtPass.getText();

        if (id.isEmpty() || name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            showAlert("Vui lòng nhập đủ thông tin!");
            return;
        }

        Teacher t = new Teacher(id, name, email, 2, user, pass);

        teacherDAO.addTeacher(t);
        System.out.println("Đã thêm giáo viên thành công !");

        loadData();
        clearFields();
    }
    @FXML
    private void updateTeacher() {
        String id = txtId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String user = txtUser.getText();
        String pass = txtPass.getText();

        if (id.isEmpty() || name.isEmpty()) {
            showAlert("Vui lòng chọn giáo viên cần sửa!");
            return;
        }
        Teacher t = new Teacher(id, name, email, 2, user, pass);
        teacherDAO.updateTeacher(t);
        showAlert("Cập nhật thành công!");
        loadData();
        handleClear();
    }
    @FXML
    private void handleClear() {
        txtId.clear();
        txtName.clear();
        txtEmail.clear();
        txtUser.clear();
        txtPass.clear();

        txtId.setDisable(false);
        tableTeacher.getSelectionModel().clearSelection();
        
        selectedTeacherForReset = null;
        lblSelectedUser.setText("Chọn giáo viên từ bảng trên");
        lblSelectedUser.setStyle("-fx-font-style: italic;");
        txtNewPassword.clear();
    }
    
    @FXML
    private void resetPassword() {
        if (selectedTeacherForReset == null) {
            showAlert("Vui lòng chọn giáo viên cần đặt lại mật khẩu!");
            return;
        }
        
        String newPassword = txtNewPassword.getText();
        if (newPassword.isEmpty() || newPassword.length() < 4) {
            showAlert("Mật khẩu mới phải có ít nhất 4 ký tự!");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận đặt lại mật khẩu");
        confirm.setHeaderText("Đặt lại mật khẩu cho: " + selectedTeacherForReset.getUsername());
        confirm.setContentText("Bạn có chắc chắn muốn đặt lại mật khẩu?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            boolean success = userDAO.resetPasswordByUsername(selectedTeacherForReset.getUsername(), newPassword);
            if (success) {
                showAlert("Đặt lại mật khẩu thành công!");
                txtNewPassword.clear();
            } else {
                showAlert("Có lỗi xảy ra khi đặt lại mật khẩu!");
            }
        }
    }
    // Trong TeacherController.java

    @FXML
    private void delTeacher() {
        Teacher selected = tableTeacher.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn giáo viên cần xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Cảnh báo nguy hiểm!");
        alert.setContentText("Bạn có chắc chắn muốn xóa giáo viên: " + selected.getFullName() +
                "\nvà tài khoản: " + selected.getUsername() + " không?");

        if (alert.showAndWait().get() == ButtonType.OK) {

            teacherDAO.deleteTeacher(selected.getTeacherId());


            loadData();
            handleClear();

            showAlert("Đã xóa thành công!");
        }
    }

    @FXML
    private void refresh() {
        loadData();
    }
    private void clearFields() {
        txtId.clear(); txtName.clear(); txtEmail.clear();
    }
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Thông báo");
        alert.setContentText(msg);
        alert.show();
    }
}
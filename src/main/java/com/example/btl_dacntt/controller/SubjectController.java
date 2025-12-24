package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.SubjectDAO;
import com.example.btl_dacntt.model.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class SubjectController {

    @FXML private TableView<Subject> tableSubject;
    @FXML private TableColumn<Subject, String> colId;
    @FXML private TableColumn<Subject, String> colName;
    @FXML private TableColumn<Subject, Integer> colCredits;

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtCredits;

    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final ObservableList<Subject> subjectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("subjectId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

        // Sự kiện khi click vào dòng trong bảng
        tableSubject.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtId.setText(newVal.getSubjectId());
                txtName.setText(newVal.getSubjectName());
                txtCredits.setText(String.valueOf(newVal.getCredits()));
                txtId.setDisable(true); // Không cho sửa mã khi đang chọn
            }
        });

        loadData();
    }

    private void loadData() {
        subjectList.clear();
        subjectList.addAll(subjectDAO.getAllSubjects());
        tableSubject.setItems(subjectList);
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) return;

        try {
            Subject s = new Subject(
                    txtId.getText(),
                    txtName.getText(),
                    Integer.parseInt(txtCredits.getText())
            );
            subjectDAO.addSubject(s);
            loadData();
            handleClear();
            showAlert("Thêm môn học thành công!");
        } catch (NumberFormatException e) {
            showAlert("Số tín chỉ phải là số nguyên!");
        }
    }

    @FXML
    private void handleUpdate() {
        Subject selected = tableSubject.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn môn học cần sửa!");
            return;
        }
        if (!validateInput()) return;

        try {
            selected.setSubjectName(txtName.getText());
            selected.setCredits(Integer.parseInt(txtCredits.getText()));

            subjectDAO.updateSubject(selected);
            loadData();
            handleClear();
            showAlert("Cập nhật thành công!");
        } catch (NumberFormatException e) {
            showAlert("Số tín chỉ phải là số nguyên!");
        }
    }

    @FXML
    private void handleDelete() {
        Subject selected = tableSubject.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn môn học cần xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("CẢNH BÁO!");
        alert.setContentText("Huynh có chắc muốn xóa môn: " + selected.getSubjectName() + "?\nDữ liệu điểm liên quan có thể bị ảnh hưởng!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            subjectDAO.deleteSubject(selected.getSubjectId());
            loadData();
            handleClear();
            showAlert("Đã xóa thành công!");
        }
    }

    @FXML
    private void handleClear() {
        txtId.clear();
        txtName.clear();
        txtCredits.clear();
        txtId.setDisable(false);
        tableSubject.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || txtCredits.getText().isEmpty()) {
            showAlert("Vui lòng nhập đủ thông tin!");
            return false;
        }
        return true;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
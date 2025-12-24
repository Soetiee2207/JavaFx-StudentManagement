package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.StudentDAO;
import com.example.btl_dacntt.model.Student;
import com.example.btl_dacntt.model.StudentFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList; // Import cái này
import javafx.collections.transformation.SortedList;   // Import cái này
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.Optional;

public class StudentController {
    @FXML private TableView<Student> tableStudent;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, LocalDate> colDob;
    @FXML private TableColumn<Student, String> colClass;
    @FXML private TableColumn<Student, String> colType;

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private DatePicker dpDob;
    @FXML private TextField txtClass;
    @FXML private ComboBox<String> cbType;

    @FXML private TextField txtSearch;

    private final StudentDAO studentDAO = new StudentDAO();
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullNameStudent"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("classId"));

        colType.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTypeString()));

        cbType.getItems().addAll("Regular", "InService");

        tableStudent.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtId.setText(newVal.getStudentId());
                txtName.setText(newVal.getFullNameStudent());
                dpDob.setValue(newVal.getDateOfBirth());
                txtClass.setText(newVal.getClassId());
                cbType.setValue(newVal.getTypeString());

                txtId.setDisable(true);
                cbType.setDisable(true);
            }
        });

        loadData();
    }

    private void loadData() {
        studentList.clear();
        studentList.addAll(studentDAO.getAllStudents());

        FilteredList<Student> filteredData = new FilteredList<>(studentList, b -> true);


        if (txtSearch != null) {
            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(student -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (student.getStudentId().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Tìm thấy
                    }

                    return false;
                });
            });
        }

        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStudent.comparatorProperty());

        tableStudent.setItems(sortedData);
    }

    @FXML
    private void addStudent() {
        if (!validateInput()) return;
        String type = cbType.getValue();
        Student s = StudentFactory.createStudent(type);

        if (s != null) {
            s.setStudentId(txtId.getText());
            s.setFullNameStudent(txtName.getText());
            s.setDateOfBirth(dpDob.getValue());
            s.setClassId(txtClass.getText());

            studentDAO.addStudent(s);

            loadData();
            handleClear();
        }
    }

    @FXML
    private void updateStudent() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn sinh viên cần sửa!");
            return;
        }
        selected.setFullNameStudent(txtName.getText());
        selected.setDateOfBirth(dpDob.getValue());
        selected.setClassId(txtClass.getText());

        studentDAO.updateStudent(selected);

        showAlert("Cập nhật thành công!");
        loadData();
        handleClear();
    }

    @FXML
    private void delStudent() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Vui lòng chọn sinh viên cần xóa!");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setContentText("Xóa SV: " + selected.getFullNameStudent() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentDAO.deleteStudent(selected.getStudentId());
            loadData();
            handleClear();
            showAlert("Đã xóa thành công!");
        }
    }

    @FXML
    private void handleClear() {
        txtId.clear();
        txtName.clear();
        dpDob.setValue(null);
        txtClass.clear();
        cbType.setValue(null);

        txtId.setDisable(false);
        cbType.setDisable(false);
        tableStudent.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() || cbType.getValue() == null) {
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
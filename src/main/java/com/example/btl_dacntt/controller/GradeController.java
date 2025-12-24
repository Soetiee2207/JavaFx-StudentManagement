package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.GradeDAO;
import com.example.btl_dacntt.dao.SubjectDAO;
import com.example.btl_dacntt.model.*;
import com.example.btl_dacntt.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class GradeController implements Initializable {

    @FXML private TableView<Grade> tableGrade;
    @FXML private TableColumn<Grade, String> colStudentId;
    @FXML private TableColumn<Grade, String> colStudentName;
    @FXML private TableColumn<Grade, String> colStudentType;
    @FXML private TableColumn<Grade, String> colSubjectName;
    @FXML private TableColumn<Grade, Float> colScoreCC;
    @FXML private TableColumn<Grade, Float> colScoreMid;
    @FXML private TableColumn<Grade, Float> colScoreFinal;
    @FXML private TableColumn<Grade, Float> colScoreTotal;

    @FXML private TextField txtStudentId;
    @FXML private Label lblStudentName;
    @FXML private Label lblStudentType;
    @FXML private ComboBox<Subject> cbSubject;

    @FXML private TextField txtScoreCC;
    @FXML private TextField txtScoreMid;
    @FXML private TextField txtScoreFinal;

    @FXML private TextField txtSearch;

    private ObservableList<Grade> gradeList = FXCollections.observableArrayList();
    private GradeDAO gradeDAO;
    private SubjectDAO subjectDAO;

    // BỎ BIẾN private Connection connect; ĐI NHÉ!
    // Vì giữ nó lâu là nó bị đóng đấy.

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gradeDAO = new GradeDAO();
        subjectDAO = new SubjectDAO();

        setupTable();
        loadSubjects();
        loadData();

        tableGrade.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillForm(newVal);
        });

        txtStudentId.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) findStudentInfo();
        });
    }

    private void setupTable() {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colStudentType.setCellValueFactory(new PropertyValueFactory<>("studentType"));
        colSubjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colScoreCC.setCellValueFactory(new PropertyValueFactory<>("scoreCc"));
        colScoreMid.setCellValueFactory(new PropertyValueFactory<>("scoreGk"));
        colScoreFinal.setCellValueFactory(new PropertyValueFactory<>("scoreCk"));
        colScoreTotal.setCellValueFactory(new PropertyValueFactory<>("scoreTk"));
    }

    private void loadData() {
        gradeList = FXCollections.observableArrayList(gradeDAO.getAllGrades());
        FilteredList<Grade> filteredData = new FilteredList<>(gradeList, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(grade -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (grade.getStudentId().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (grade.getSubjectName() != null && grade.getSubjectName().toLowerCase().contains(lowerCaseFilter)) return true;
                return false;
            });
        });

        SortedList<Grade> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableGrade.comparatorProperty());
        tableGrade.setItems(sortedData);
    }

    private void loadSubjects() {
        ObservableList<Subject> subjects = FXCollections.observableArrayList(subjectDAO.getAllSubjects());
        cbSubject.setItems(subjects);
        cbSubject.setConverter(new StringConverter<Subject>() {
            @Override
            public String toString(Subject s) { return s == null ? null : s.getSubjectId() + " - " + s.getSubjectName(); }
            @Override
            public Subject fromString(String string) { return null; }
        });
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) return;
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {
            String type = lblStudentType.getText();
            if (type == null || type.isEmpty() || type.equals("...")) {
                findStudentInfo();
                type = lblStudentType.getText();
                if (type.equals("...")) return;
            }

            String factoryType = convertTypeForFactory(type);
            Student student = StudentFactory.createStudent(factoryType);

            float cc = Float.parseFloat(txtScoreCC.getText());
            float gk = Float.parseFloat(txtScoreMid.getText());
            float ck = Float.parseFloat(txtScoreFinal.getText());
            float tk = student.calculateAverage(cc, gk, ck);

            String sql = "INSERT INTO grades (student_id, subject_id, score_cc, score_gk, score_ck, score_tk) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtStudentId.getText());
            pst.setString(2, cbSubject.getValue().getSubjectId());
            pst.setFloat(3, cc);
            pst.setFloat(4, gk);
            pst.setFloat(5, ck);
            pst.setFloat(6, tk);

            pst.executeUpdate();
            showAlert("Thành công", "Đã thêm mới. TK: " + tk, Alert.AlertType.INFORMATION);
            loadData();
            clearForm();

        } catch (Exception e) {
            showAlert("Lỗi", "Lỗi: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Grade selected = tableGrade.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Cảnh báo", "Chọn dòng cần sửa!", Alert.AlertType.WARNING);
            return;
        }

        Connection conn = DatabaseConnection.getInstance().getConnection(); // <--- LẤY KẾT NỐI MỚI

        try {
            String factoryType = convertTypeForFactory(selected.getStudentType());
            Student student = StudentFactory.createStudent(factoryType);

            float cc = Float.parseFloat(txtScoreCC.getText());
            float gk = Float.parseFloat(txtScoreMid.getText());
            float ck = Float.parseFloat(txtScoreFinal.getText());
            float tk = student.calculateAverage(cc, gk, ck);

            String sql = "UPDATE grades SET score_cc=?, score_gk=?, score_ck=?, score_tk=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setFloat(1, cc);
            pst.setFloat(2, gk);
            pst.setFloat(3, ck);
            pst.setFloat(4, tk);
            pst.setInt(5, selected.getId());

            pst.executeUpdate();
            showAlert("Thành công", "Cập nhật xong.", Alert.AlertType.INFORMATION);
            loadData();
            clearForm();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleDelete() {
        Grade selected = tableGrade.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setContentText("Xóa bản ghi này?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Connection conn = DatabaseConnection.getInstance().getConnection(); // <--- LẤY KẾT NỐI MỚI
            try {
                String sql = "DELETE FROM grades WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, selected.getId());
                pst.executeUpdate();
                loadData();
                clearForm();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void findStudentInfo() {
        String id = txtStudentId.getText();
        if (id.isEmpty()) return;

        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {
            String sql = "SELECT full_name, type FROM students WHERE student_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lblStudentName.setText(rs.getString("full_name"));
                lblStudentType.setText(rs.getString("type"));
            } else {
                lblStudentName.setText("Không tìm thấy!");
                lblStudentType.setText("...");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Các hàm phụ trợ giữ nguyên
    private void fillForm(Grade g) {
        txtStudentId.setText(g.getStudentId());
        txtStudentId.setDisable(true);
        for(Subject s : cbSubject.getItems()){
            if(s.getSubjectId().equals(g.getSubjectId())){
                cbSubject.setValue(s);
                break;
            }
        }
        cbSubject.setDisable(true);
        txtScoreCC.setText(String.valueOf(g.getScoreCc()));
        txtScoreMid.setText(String.valueOf(g.getScoreGk()));
        txtScoreFinal.setText(String.valueOf(g.getScoreCk()));
        lblStudentName.setText(g.getStudentName());
        lblStudentType.setText(g.getStudentType());
    }

    @FXML
    private void clearForm() {
        txtStudentId.setText("");
        txtStudentId.setDisable(false);
        cbSubject.setValue(null);
        cbSubject.setDisable(false);
        txtScoreCC.setText("");
        txtScoreMid.setText("");
        txtScoreFinal.setText("");
        lblStudentName.setText("...");
        tableGrade.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        if (txtStudentId.getText().isEmpty() || cbSubject.getValue() == null) {
            showAlert("Thiếu", "Nhập Mã SV và Chọn Môn học", Alert.AlertType.WARNING);
            return false;
        }
        try {
            if(txtScoreCC.getText().isEmpty()) txtScoreCC.setText("0");
            if(txtScoreMid.getText().isEmpty()) txtScoreMid.setText("0");
            if(txtScoreFinal.getText().isEmpty()) txtScoreFinal.setText("0");

            float cc = Float.parseFloat(txtScoreCC.getText());
            float gk = Float.parseFloat(txtScoreMid.getText());
            float ck = Float.parseFloat(txtScoreFinal.getText());
            if (cc < 0 || cc > 10 || gk < 0 || gk > 10 || ck < 0 || ck > 10) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Điểm phải là số 0-10", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private String convertTypeForFactory(String dbType) {
        if (dbType == null) return "Unknown";
        if (dbType.equalsIgnoreCase("Chính quy") || dbType.equalsIgnoreCase("Regular")) return "Regular";
        if (dbType.equalsIgnoreCase("Tại chức") || dbType.equalsIgnoreCase("InService")) return "InService";
        return dbType;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setContentText(content); a.show();
    }
}
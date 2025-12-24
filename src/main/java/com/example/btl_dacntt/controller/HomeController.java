package com.example.btl_dacntt.controller;

import com.example.btl_dacntt.dao.GradeDAO;
import com.example.btl_dacntt.dao.StudentDAO;
import com.example.btl_dacntt.dao.SubjectDAO;
import com.example.btl_dacntt.model.Grade;
import com.example.btl_dacntt.model.Student;
import com.example.btl_dacntt.model.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeController {

    @FXML private Label lblTotalStudents;
    @FXML private Label lblTotalSubjects;

    @FXML private Label lblExcellent;
    @FXML private Label lblGood;
    @FXML private Label lblFair;
    @FXML private Label lblAverage;
    @FXML private Label lblWeak;

    @FXML private PieChart pieChartGrade;

    private final StudentDAO studentDAO = new StudentDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final GradeDAO gradeDAO = new GradeDAO();
    @FXML
    public void initialize() {
        calculateAndDisplayStats();
    }

    private void calculateAndDisplayStats() {
        List<Student> students = studentDAO.getAllStudents();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        List<Grade> grades = gradeDAO.getAllGrades();

        lblTotalStudents.setText(String.valueOf(students.size()));
        lblTotalSubjects.setText(String.valueOf(subjects.size()));

        Map<String, Integer> creditMap = new HashMap<>();
        for (Subject s : subjects) {
            creditMap.put(s.getSubjectId(), s.getCredits());
        }

        Map<String, List<Grade>> studentGradesMap = grades.stream()
                .collect(Collectors.groupingBy(Grade::getStudentId));

        int countExcellent = 0;
        int countGood = 0;
        int countFair = 0;
        int countAvg = 0;
        int countWeak = 0;

        for (Student s : students) {
            List<Grade> myGrades = studentGradesMap.get(s.getStudentId());

            if (myGrades == null || myGrades.isEmpty()) {
                continue;
            }

            double totalScorePoints = 0;
            int totalCredits = 0;

            for (Grade g : myGrades) {

                Integer credits = creditMap.get(g.getSubjectId());

                if (credits != null) {
                    totalScorePoints += g.getScoreTk() * credits;
                    totalCredits += credits;
                }
            }

            double cpa = (totalCredits == 0) ? 0 : (totalScorePoints / totalCredits);

            if (cpa >= 9.0) {
                countExcellent++;
            } else if (cpa >= 8.0) {
                countGood++;
            } else if (cpa >= 7.0) {
                countFair++;
            } else if (cpa >= 5.0) {
                countAvg++;
            } else {
                countWeak++;
            }
        }

        lblExcellent.setText(String.valueOf(countExcellent));
        lblGood.setText(String.valueOf(countGood));
        lblFair.setText(String.valueOf(countFair));
        lblAverage.setText(String.valueOf(countAvg));
        lblWeak.setText(String.valueOf(countWeak));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Xuất sắc", countExcellent),
                new PieChart.Data("Giỏi", countGood),
                new PieChart.Data("Khá", countFair),
                new PieChart.Data("Trung bình", countAvg),
                new PieChart.Data("Yếu", countWeak)
        );

        pieChartGrade.setData(pieData.filtered(d -> d.getPieValue() > 0));

        pieChartGrade.getData().forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / students.size() * 100));
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int)data.getPieValue() + " SV (" + percentage + ")");
            Tooltip.install(data.getNode(), tooltip);
        });
    }
}

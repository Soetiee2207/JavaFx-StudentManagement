package com.example.btl_dacntt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Hệ Thống Quản Lý Sinh Viên - Đăng Nhập");
        stage.setScene(scene);
        stage.show();
    }
}

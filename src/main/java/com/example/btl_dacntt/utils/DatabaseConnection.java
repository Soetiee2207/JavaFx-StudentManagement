package com.example.btl_dacntt.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection conn;

    private static final String URL = "jdbc:mysql://localhost:3306/quanlysinhvien";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection(){
        try {
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static DatabaseConnection getInstance(){
        if (instance == null){
            instance = new DatabaseConnection();
        } else{
            try{
                if (instance.conn.isClosed()){
                    instance = new DatabaseConnection();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return instance;
    }
    public Connection getConnection(){
        return conn;
    }
}

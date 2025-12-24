module com.example.btl_dacntt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.sql;
    requires static lombok;
    requires jbcrypt;
    opens com.example.btl_dacntt to javafx.fxml;
    opens com.example.btl_dacntt.controller to javafx.fxml;
    exports com.example.btl_dacntt;
    exports com.example.btl_dacntt.controller;
    exports com.example.btl_dacntt.model;
    exports com.example.btl_dacntt.utils;
}
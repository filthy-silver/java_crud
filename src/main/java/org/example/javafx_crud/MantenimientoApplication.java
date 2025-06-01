package org.example.javafx_crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MantenimientoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MantenimientoApplication.class.getResource("mantenimiento.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        MantenimientoController controller = fxmlLoader.getController();

        controller.setConexion(conexion());

        controller.cargarDatos();

        stage.setTitle("Mantenimiento de Estudiantes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        conexion();
        launch(args);
    }

    public static Connection conexion() {
        Connection conexion;
        String db = "prueba";
        String host = "jdbc:mariadb://localhost:3307/";
        String user = "root";
        String password = "";
        System.out.println("Conectando ...");

        try{
            conexion = DriverManager.getConnection(host + db, user, password);
            System.out.println("Conectado con exito");
        } catch (SQLException e) {
            System.out.println((e.getMessage()));
            throw new RuntimeException(e);
        }
        return conexion;
    }
}
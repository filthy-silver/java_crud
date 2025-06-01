package org.example.javafx_crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;

public class RepositorioEstudiantes {
    private static final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();


    public static ObservableList<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public static void agregarEstudiante(Estudiante est) {
        estudiantes.add(est);
    }

    public static void cargarEstudiantesDesdeDB(Connection conexion) {
        estudiantes.clear();

        String query = "SELECT * FROM estudiantes";

        Statement stmt;
        ResultSet respuesta;

        try {
            stmt = conexion.createStatement();
            respuesta = stmt.executeQuery(query);

            while (respuesta.next()) {
                int nia = respuesta.getInt("nia");
                String nombre = respuesta.getString("nombre");
                LocalDate nacimiento = respuesta.getDate("fecha_nacimiento").toLocalDate();

                Estudiante estudiante = new Estudiante(nia, nombre, nacimiento);
                estudiantes.add(estudiante);
            }

            System.out.println("Cargados " + estudiantes.size() + " estudiantes desde la base de datos");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
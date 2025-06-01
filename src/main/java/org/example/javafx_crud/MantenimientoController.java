package org.example.javafx_crud;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;

public class MantenimientoController {
    @FXML
    public TextField tf_nia;
    @FXML
    public TextField tf_nombre;
    @FXML
    public DatePicker tf_fecha;
    @FXML
    public TableView<Estudiante> tw_estudiantes;
    @FXML
    public TableColumn<Estudiante, String> nombreColumna;
    @FXML
    public TableColumn<Estudiante, Integer> NIAColumna;
    @FXML
    public TableColumn<Estudiante, LocalDate> fechaNacimientoColumna;
    @FXML
    public Button btn_guardar;
    @FXML
    public Button btn_insertar;

    @FXML
    private Connection conexion;

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    @FXML
    public void initialize() {
        NIAColumna.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getNia()).asObject());
        nombreColumna.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre()));
        fechaNacimientoColumna.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getFecha_nacimiento()));

        tw_estudiantes.setItems(RepositorioEstudiantes.getEstudiantes());
    }

    public void cargarDatos() {
        if (conexion != null) {
            RepositorioEstudiantes.cargarEstudiantesDesdeDB(conexion);
        }
    }

    public void guardar(ActionEvent actionEvent) {
        String niaText = tf_nia.getText();
        String nombreText = tf_nombre.getText();
        LocalDate fechaNacimiento = tf_fecha.getValue();

        if (niaText.isEmpty() || nombreText.isEmpty() || fechaNacimiento == null) {
            System.out.println("Error: Todos los campos deben ser completados.");
            return;
        }

        try {
            int nia = Integer.parseInt(niaText);
            String query = "UPDATE estudiantes SET nombre = '" + nombreText + "', fecha_nacimiento = '" + fechaNacimiento + "' WHERE nia = " + nia;
            System.out.println("Ejecutando consulta: " + query);

            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(query);

            tf_nia.clear();
            tf_nombre.clear();
            tf_fecha.setValue(null);

            btn_guardar.setDisable(true);
            btn_insertar.setDisable(false);

            cargarDatos();
            System.out.println("Estudiante actualizado: NIA=" + nia + ", Nombre=" + nombreText);

        } catch (SQLException e) {
            System.out.println("Error al realizar la consulta: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Error: NIA debe ser un número válido.");
            tf_nia.clear();
            tf_nombre.clear();
            tf_fecha.setValue(null);
        }
    }

    public void insertar(ActionEvent actionEvent) {
        String niaText = tf_nia.getText();
        String nombreText = tf_nombre.getText();
        LocalDate fechaNacimiento = tf_fecha.getValue();

        if (niaText.isEmpty() || nombreText.isEmpty() || fechaNacimiento == null) {
            System.out.println("Error: Todos los campos deben ser completados.");
            return;
        }

        try {
            int nia = Integer.parseInt(niaText);
            String query = "INSERT INTO estudiantes (nia, nombre, fecha_nacimiento) VALUES (" + nia + ", '" + nombreText + "', '" + fechaNacimiento + "')";
            System.out.println("Ejecutando consulta: " + query);

            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(query);

            tf_nia.clear();
            tf_nombre.clear();
            tf_fecha.setValue(null);

            cargarDatos();
            System.out.println("Estudiante insertado: NIA=" + nia + ", Nombre=" + nombreText);

        } catch (SQLException e) {
            System.out.println("Error al realizar la consulta: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Error: NIA debe ser un número válido.");
            tf_nia.clear();
            tf_nombre.clear();
            tf_fecha.setValue(null);
        }
    }

    public void editar(ActionEvent actionEvent) {
        Estudiante seleccionado = tw_estudiantes.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            tf_nia.setText(String.valueOf(seleccionado.getNia()));
            tf_nombre.setText(seleccionado.getNombre());
            tf_fecha.setValue(seleccionado.getFecha_nacimiento());

            btn_guardar.setDisable(false);
            btn_insertar.setDisable(true);
        } else {
            System.out.println("No hay ninguna fila seleccionada.");
        }
    }

    public void eliminar(ActionEvent actionEvent) {
        Estudiante seleccionado = tw_estudiantes.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            String query = "DELETE FROM estudiantes WHERE nia = " + seleccionado.getNia();
            System.out.println("Ejecutando consulta: " + query);

            try {
                Statement stmt = conexion.createStatement();
                stmt.executeUpdate(query);

                RepositorioEstudiantes.getEstudiantes().remove(seleccionado);
                System.out.println("Estudiante eliminado: " + seleccionado.getNombre());

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No hay ninguna fila seleccionada.");
        }
    }
}
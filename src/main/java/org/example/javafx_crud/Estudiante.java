package org.example.javafx_crud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
public class Estudiante {
    private int nia;
    private String nombre;
    private LocalDate fecha_nacimiento;
}



package com.facultad;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "profesor")
public class Profesor extends Persona implements Serializable {
    @Column(name = "antiguedad")
    private int antiguedad;

    // Constructores
    public Profesor() {}

    public Profesor(String apellido, String nombre, String dni, Date fechaNacimiento, Ciudad ciudad, int antiguedad) {
        super(apellido, nombre, dni, fechaNacimiento, ciudad);
        this.antiguedad = antiguedad;
    }

    Profesor(String nombre, int antiguedad) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    // Getters y Setters
    public int getAntiguedad() { return antiguedad; }
    public void setAntiguedad(int antiguedad) { this.antiguedad = antiguedad; }

    // toString

    @Override
    public String toString() {
        return "Profesor{" + "antiguedad=" + antiguedad + '}';
    }
}

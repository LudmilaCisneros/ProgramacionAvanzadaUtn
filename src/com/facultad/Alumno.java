package com.facultad;

import com.facultad.*;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alumno")
public class Alumno extends Persona {

    @Column(name = "numero_legajo")
    private String numeroLegajo;

    @Column(name = "anio_ingreso")
    private int anioIngreso;

    @ManyToMany(mappedBy = "alumnos")
    private Set<Materia> materias = new HashSet<>();

    // Constructores
    public Alumno() {}

    public Alumno(String apellido, String nombre, String dni, Date fechaNacimiento, Ciudad ciudad, String numeroLegajo, int anioIngreso) {
        super(apellido, nombre, dni, fechaNacimiento, ciudad);
        this.numeroLegajo = numeroLegajo;
        this.anioIngreso = anioIngreso;
    }

    // Método para agregar una materia al alumno
    public void agregarMateria(Materia materia) {
        materias.add(materia);
        materia.getAlumnos().add(this); // Sincroniza la rel Materia
    }

    public void removerMateria(Materia materia) {
        materias.remove(materia);
        materia.getAlumnos().remove(this); // Sincroniza la eliminación en Materia
    }

    // Getters y setters
    public String getNumeroLegajo() { return numeroLegajo; }
    public void setNumeroLegajo(String numeroLegajo) { this.numeroLegajo = numeroLegajo; }

    public int getAnioIngreso() { return anioIngreso; }
    public void setAnioIngreso(int anioIngreso) { this.anioIngreso = anioIngreso; }

    public Set<Materia> getMaterias() { return materias; }
    public void setMaterias(Set<Materia> materias) { this.materias = materias; }

    @Override
    public String toString() {
        return "Alumno [ID=" + getId() + ", Apellido=" + getApellido() + ", Nombre=" + getNombre() +
               ", DNI=" + getDni() + ", Fecha de Nacimiento=" + getFechaNacimiento() +
               ", Ciudad=" + (getCiudad() != null ? getCiudad().getNombre() : "N/A") +
               ", Número de Legajo=" + numeroLegajo + ", Año de Ingreso=" + anioIngreso + "]";
    }
}

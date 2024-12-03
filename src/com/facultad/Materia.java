
package com.facultad;

import com.facultad.*;
import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "materia")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nivel")
    private String nivel;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @ManyToOne
    @JoinColumn(name = "carrera_id")  // Define la columna de unión para la relación con Carrera
    private Carrera carrera;

    @ManyToMany
    @JoinTable(name = "materia_alumno",
            joinColumns = @JoinColumn(name = "materia_id"),
            inverseJoinColumns = @JoinColumn(name = "alumno_id"))
    private Set<Alumno> alumnos = new HashSet<>();

    // Constructores, getters y setters
    public Materia() {}

    public Materia(String nombre, String nivel, Profesor profesor, Carrera carrera) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.profesor = profesor;
        this.carrera = carrera;
    }

    @Override
    public String toString() {
        return "Materia[" + "id=" + id + ", nombre=" + nombre + ", nivel=" + nivel + ", carrera=" + carrera +']';
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }

    public Set<Alumno> getAlumnos() { return alumnos; }
    public void setAlumnos(Set<Alumno> alumnos) { this.alumnos = alumnos; }
}

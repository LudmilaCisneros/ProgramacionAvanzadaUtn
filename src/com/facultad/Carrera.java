package com.facultad;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "carrera")
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL)  // mappedBy debe coincidir con el nombre de la propiedad en Materia
    private List<Materia> materias;

    // Constructores, getters y setters
    public Carrera() {}

    public Carrera(String nombre, Facultad facultad) {
        this.nombre = nombre;
        this.facultad = facultad;
    }

    @Override
    public String toString() {
        return "Carrera{" + "id=" + id + ", nombre=" + nombre +'}';
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }

    public List<Materia> getMaterias() { return materias; }
    public void setMaterias(List<Materia> materias) { this.materias = materias; }
}

package com.facultad;

import java.io.Serializable;
import javax.persistence.*;

// Entidad que representa una ciudad mapeada a tabla bd.

@Entity
@Table(name = "ciudad")  // Definimos el nombre de la tabla en la base de datos
public class Ciudad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática de la clave primaria
    private Long id;  // "Long" y no long, para soportar valores nulos

    
    
    
    @Column(name = "nombre", nullable = false, length = 100)  // Especificamos longitud máxima para "nombre"
    private String nombre;

    //Constructor vacío. Requerido por Hibernate.
     
    public Ciudad() {
    }

    // Constructor para inicializar la ciudad con un nombre.
     
    public Ciudad(String nombre) {
        this.nombre = nombre;
    }

    //Obtiene el ID de la ciudad.
     
    public Long getId() {
        return id;
    }

    // Establece el ID de la ciudad.
     
    public void setId(Long id) {
        this.id = id;
    }

    // Obtiene el nombre de la ciudad.
     
    public String getNombre() {
        return nombre;
    }

    // Establece el nombre de la ciudad.
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Representa la entidad Ciudad en forma de texto.
    
    @Override
    public String toString() {
        return "Ciudad{" + "id=" + id + ", nombre='" + nombre + '\'' + '}';
    }
}

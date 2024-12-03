package com.facultad.Dao;

import com.facultad.*;
import org.hibernate.Session;
import java.util.List;

public class AlumnoDAO {

    // Listar todos los alumnos
    @SuppressWarnings("unchecked")
    public List<Alumno> listarAlumnos(Session session) {
        return session.createQuery("FROM Alumno").list();
    }

    // Obtener alumnos ordenados por apellido
    @SuppressWarnings("unchecked")
    public List<Alumno> obtenerAlumnosOrdenadosPorApellido(Session session) {
    return session.createQuery("FROM Alumno a ORDER BY a.apellido").list();
}


    // Crear un alumno
    public void crearAlumno(Session session, Alumno alumno) {
        session.save(alumno);
    }

    // Actualizar un alumno
    public void actualizarAlumno(Session session, Alumno alumno) {
        session.update(alumno);
    }

    // Borrar un alumno
    public void borrarAlumno(Session session, Alumno alumno) {
        session.delete(alumno);
    }
}

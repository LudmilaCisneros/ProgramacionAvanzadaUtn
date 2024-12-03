package com.facultad.Dao;

import com.facultad.Profesor;
import org.hibernate.Session;
import java.util.List;

public class ProfesorDAO {

    // Listar todos los profesores ordenados por antigüedad
    @SuppressWarnings("unchecked")
    public List<Profesor> obtenerProfesoresOrdenadosPorAntiguedad(Session session) {
        return session.createQuery("FROM Profesor ORDER BY antiguedad DESC").list();
    }

    // Listar todos los profesores
    @SuppressWarnings("unchecked")
    public List<Profesor> listarProfesores(Session session) {
        return session.createQuery("FROM Profesor").list();
    }

    // Crear un profesor
    public void crearProfesor(Session session, Profesor profesor) {
        session.save(profesor);
    }

    // Actualizar un profesor
    public void actualizarProfesor(Session session, Profesor profesor) {
        session.update(profesor);
    }

    // Borrar un profesor
    public void borrarProfesor(Session session, Profesor profesor) {
        session.delete(profesor);
    }
}

package com.facultad.Dao;

import com.facultad.Materia;
import org.hibernate.Session;
import java.util.List;

public class MateriaDAO {

    // Guardar una materia
    public void crearMateria(Session session, Materia materia) {
        session.save(materia);
    }

    // Obtener materias por nivel
    @SuppressWarnings("unchecked")
    public List<Materia> obtenerMateriasPorNivel(Session session, String nivel) {
        return session.createQuery("FROM Materia WHERE nivel = :nivel")
                      .setParameter("nivel", nivel)
                      .list();
    }

    // Listar todas las materias
    @SuppressWarnings("unchecked")
    public List<Materia> listarMaterias(Session session) {
        return session.createQuery("FROM Materia").list();
    }

    // Actualizar una materia
    public void actualizarMateria(Session session, Materia materia) {
        session.update(materia);
    }

    // Borrar una materia
    public void borrarMateria(Session session, Materia materia) {
        session.delete(materia);
    }
}

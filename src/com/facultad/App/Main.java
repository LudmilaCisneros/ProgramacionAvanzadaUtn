package com.facultad.App;

import com.facultad.*;
import com.facultad.Alumno;
import com.facultad.Carrera;
import com.facultad.Ciudad;
import com.facultad.Dao.AlumnoDAO;
import com.facultad.Dao.ProfesorDAO;
import com.facultad.Dao.MateriaDAO;
import com.facultad.Facultad;
import com.facultad.Materia;
import com.facultad.Profesor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Date;
import java.util.List;

public class Main {

    private static AlumnoDAO alumnoDAO = new AlumnoDAO();
    private static ProfesorDAO profesorDAO = new ProfesorDAO();
    private static MateriaDAO materiaDAO = new MateriaDAO();
    private static SessionFactory sessionFactory;
    
    public static void main(String[] args) {
        crearBaseDeDatos();
        sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        crearDatos(session);
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            mostrarMenu();
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir nueva línea

            switch (option) {
                case 1:
                    consultarAlumnosOrdenados(session);
                    break;
                case 2:
                    inscribirAlumnoEnMaterias(session, scanner);
                    break;
                case 3:
                    consultarProfesoresOrdenados(session);
                    break;
                case 4:
                    consultarMateriasPorNivel(session, scanner);
                    break;
                case 5:
                    crudAlumnos(session, scanner);
                    break;
                case 6:
                    crudProfesores(session, scanner);
                    break;
                case 7:
                    crudMaterias(session, scanner);
                    break;
                case 8:
                    crearCiudad(session, scanner);
                    break;
                case 9:
                    crearFacultad(session, scanner);
                    break;
                case 10:
                    crearCarrera(session, scanner);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        session.close();
        sessionFactory.close();
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n===== Menú Principal =====");
        System.out.println("1. Consultar alumnos ordenados por apellido");
        System.out.println("2. Inscribir alumno a materias"); 
        System.out.println("3. Consultar profesores ordenados por antigüedad");
        System.out.println("4. Consultar materias por nivel");
        System.out.println("5. CRUD Alumnos");
        System.out.println("6. CRUD Profesores");
        System.out.println("7. CRUD Materias");
        System.out.println("8. Crear Ciudad");
        System.out.println("9. Crear Facultad");
        System.out.println("10. Crear Carrera");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void crearBaseDeDatos() {
        String url = "jdbc:mariadb://localhost:3306/?useSSL=false";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS facultad";
            stmt.executeUpdate(sql);
            System.out.println("Base de datos creada o ya existente.");
        } catch (SQLException e) {
            System.err.println("Error creando la base de datos: " + e.getMessage());
        }
    }
    
    private static void crearDatos(Session session) {
        Transaction tx = session.beginTransaction();

        try {
            Ciudad ciudad = new Ciudad("Buenos Aires");
            session.save(ciudad);

            Facultad facultad = new Facultad("UTN FRBA", ciudad);
            session.save(facultad);

            Date fechaNacimiento = java.sql.Date.valueOf("1997-01-12");
            alumnoDAO.crearAlumno(session, new Alumno("Cisneros", "Ludmila", "40011452", fechaNacimiento, ciudad, "101", 2024));
            
            Date fechaNacimiento1 = java.sql.Date.valueOf("1992-02-15");
            alumnoDAO.crearAlumno(session, new Alumno("Almada", "Pedro", "36879654", fechaNacimiento1, ciudad, "102", 2023));

            Date fechaNacimiento2 = java.sql.Date.valueOf("1982-03-16");
            Profesor profesor = new Profesor("Lopez", "Juan", "30456987", fechaNacimiento2, ciudad, 2);
            profesorDAO.crearProfesor(session, profesor);
            
            Date fechaNacimiento3 = java.sql.Date.valueOf("1980-07-11");
            Profesor profesor1 = new Profesor("Juarez", "Luciana", "28789632", fechaNacimiento3, ciudad, 5);
            profesorDAO.crearProfesor(session, profesor1);

            Carrera carrera = new Carrera("Tecnicatura en Programacion", facultad);
            session.save(carrera);

            Materia materia = new Materia("Programacion", "1", profesor, carrera);
            materiaDAO.crearMateria(session, materia);
            
            Materia materia1 = new Materia("Sistemas Operativos", "1", profesor1, carrera);
            materiaDAO.crearMateria(session, materia1);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println("Se ha producido un error al cargar datos." + e.getMessage());
        }
    }
    
    private static void consultarAlumnosOrdenados(Session session) {
        System.out.println("\n--- Alumnos ordenados por Apellido ---");
        alumnoDAO.obtenerAlumnosOrdenadosPorApellido(session).forEach(System.out::println);
    }

    private static void consultarProfesoresOrdenados(Session session) {
        System.out.println("\n--- Profesores ordenados por Antigüedad ---");
        profesorDAO.obtenerProfesoresOrdenadosPorAntiguedad(session)
            .forEach(profesor -> System.out.println("Profesor [ID=" + profesor.getId() + 
                                                     ", Apellido=" + profesor.getApellido() + 
                                                     ", Nombre=" + profesor.getNombre() + 
                                                     ", DNI=" + profesor.getDni() +
                                                     ", Fecha Nacimiento=" + profesor.getFechaNacimiento() + 
                                                     ", Ciudad=" + (profesor.getCiudad() != null ? profesor.getCiudad().getNombre() : "N/A") + 
                                                     ", Antigüedad=" + profesor.getAntiguedad() + "]"));
    }

    private static void consultarMateriasPorNivel(Session session, Scanner scanner) {
        System.out.print("\nIngrese el nivel de las materias que desea consultar: ");
        String nivel = scanner.nextLine();
        System.out.println("\n--- Materias del nivel " + nivel + " ---");
        materiaDAO.obtenerMateriasPorNivel(session, nivel).forEach(System.out::println);
    }
    
    
    private static void inscribirAlumnoEnMaterias(Session session, Scanner scanner) {
    System.out.print("Ingrese el DNI del alumno que desea inscribir en materias: ");
    String dni = scanner.nextLine();

    Alumno alumno = (Alumno) session.createQuery("FROM Alumno WHERE dni = :dni")
                                     .setParameter("dni", dni)
                                     .uniqueResult();

    if (alumno == null) {
        System.out.println("Alumno no encontrado. Asegúrese de que el DNI es correcto.");
        return;
    }

    System.out.println("Alumno encontrado: " + alumno.getNombre() + " " + alumno.getApellido());
    System.out.println("Seleccione las materias en las que desea inscribir al alumno (Ingrese los nombres de las materias separados por comas):");

    @SuppressWarnings("unchecked")
    List<Materia> materias = session.createQuery("FROM Materia").list();
    if (materias.isEmpty()) {
        System.out.println("No hay materias disponibles para inscripción.");
        return;
    }

    // Mostrar todas las materias disponibles
    materias.forEach(materia -> System.out.println(" - " + materia.getNombre() + " (Nivel: " + materia.getNivel() + ")"));
    
    System.out.print("Ingrese los nombres de las materias separados por comas: ");
    String[] nombresMaterias = scanner.nextLine().split(",");

    Transaction tx = session.beginTransaction();
    try {
        for (String nombreMateria : nombresMaterias) {
            nombreMateria = nombreMateria.trim(); // Quita espacios en blanco
            Materia materia = (Materia) session.createQuery("FROM Materia WHERE nombre = :nombre")
                                               .setParameter("nombre", nombreMateria)
                                               .uniqueResult();
            if (materia != null) {
                alumno.agregarMateria(materia); // Agrega la materia al alumno
                session.update(materia); // Actualiza la materia para reflejar la relación bidireccional
                System.out.println("Alumno inscripto en la materia: " + nombreMateria);
            } else {
                System.out.println("Materia no encontrada: " + nombreMateria);
            }
        }
        tx.commit();
    } catch (Exception e) {
        tx.rollback();
        System.out.println("Error al inscribir el alumno en las materias: " + e.getMessage());
    }
}

    

    // CRUD Alumnos
    private static void crudAlumnos(Session session, Scanner scanner) {
        System.out.println("\n===== CRUD Alumnos =====");
        System.out.println("1. Crear alumno");
        System.out.println("2. Actualizar alumno");
        System.out.println("3. Borrar alumno");
        System.out.println("4. Listar todos los alumnos");
        System.out.print("Seleccione una opción: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        switch (option) {
            case 1:
                crearAlumno(session, scanner);
                break;
            case 2:
                actualizarAlumno(session, scanner);
                break;
            case 3:
                borrarAlumno(session, scanner);
                break;
            case 4:
                listarAlumnos(session);
                break;
            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    private static void crearAlumno(Session session, Scanner scanner) {
        System.out.println("Ingrese los datos del alumno:");
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Fecha de Nacimiento (YYYY-MM-DD): ");
        String fechaNacimientoStr = scanner.nextLine();
        Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);

        System.out.print("Ciudad: ");
        String nombreCiudad = scanner.nextLine();
        Ciudad ciudad = obtenerCiudadPorNombre(session, nombreCiudad);
        if (ciudad == null) return;

        System.out.print("Número de legajo: ");
        String legajo = scanner.nextLine();
        System.out.print("Año de ingreso: ");
        int anioIngreso = scanner.nextInt();
        scanner.nextLine();

        Alumno alumno = new Alumno(apellido, nombre, dni, fechaNacimiento, ciudad, legajo, anioIngreso);
        Transaction tx = session.beginTransaction();
        try {
            alumnoDAO.crearAlumno(session, alumno);
            tx.commit();
            System.out.println("Alumno creado con éxito.");
        } catch (Exception e) {
            tx.rollback();
            System.out.println("Error creando el alumno: " + e.getMessage());
        }
    }

    private static void actualizarAlumno(Session session, Scanner scanner) {
        listarAlumnos(session);
        System.out.print("Ingrese el DNI del alumno a actualizar: ");
        String dni = scanner.nextLine();

        Alumno alumno = (Alumno) session.createQuery("FROM Alumno WHERE dni = :dni")
                                        .setParameter("dni", dni)
                                        .uniqueResult();
        if (alumno != null) {
            System.out.print("Nuevo apellido (actual: " + alumno.getApellido() + "): ");
            String nuevoApellido = scanner.nextLine();
            alumno.setApellido(nuevoApellido);

            Transaction tx = session.beginTransaction();
            try {
                session.update(alumno);
                tx.commit();
                System.out.println("Alumno actualizado con éxito.");
            } catch (Exception e) {
                tx.rollback();
                System.out.println("Error actualizando el alumno: " + e.getMessage());
            }
        } else {
            System.out.println("Alumno no encontrado.");
        }
    }

    private static void borrarAlumno(Session session, Scanner scanner) {
        System.out.print("Ingrese el DNI del alumno a borrar: ");
        String dni = scanner.nextLine();

        Alumno alumno = (Alumno) session.createQuery("FROM Alumno WHERE dni = :dni")
                                        .setParameter("dni", dni)
                                        .uniqueResult();

        if (alumno != null) {
            Transaction tx = session.beginTransaction();
            try {
                session.delete(alumno);
                tx.commit();
                System.out.println("Alumno borrado con éxito.");
            } catch (Exception e) {
                tx.rollback();
                System.out.println("Error borrando el alumno: " + e.getMessage());
            }
        } else {
            System.out.println("Alumno no encontrado.");
        }
    }

    private static void listarAlumnos(Session session) {
    System.out.println("\n--- Lista de Alumnos ---");
    alumnoDAO.listarAlumnos(session).forEach(alumno -> {
        System.out.println("Alumno [ID=" + alumno.getId() + 
                           ", Apellido=" + alumno.getApellido() + 
                           ", Nombre=" + alumno.getNombre() + 
                           ", DNI=" + alumno.getDni() +
                           ", Fecha de Nacimiento=" + alumno.getFechaNacimiento() + 
                           ", Ciudad=" + (alumno.getCiudad() != null ? alumno.getCiudad().getNombre() : "N/A") + 
                           ", Número de Legajo=" + alumno.getNumeroLegajo() + 
                           ", Año de Ingreso=" + alumno.getAnioIngreso() + "]");

        // Mostrar las materias en las que el alumno está inscrito
        System.out.println("Materias inscritas:");
        alumno.getMaterias().forEach(materia -> 
            System.out.println("  - " + materia.getNombre() + " (Nivel: " + materia.getNivel() + 
                               ", Profesor: " + materia.getProfesor().getNombre() + " " + materia.getProfesor().getApellido() + ")")
        );
    });
}


    // CRUD Profesores
    private static void crudProfesores(Session session, Scanner scanner) {
        System.out.println("\n===== CRUD Profesores =====");
        System.out.println("1. Crear profesor");
        System.out.println("2. Actualizar profesor");
        System.out.println("3. Borrar profesor");
        System.out.println("4. Listar todos los profesores");
        System.out.print("Seleccione una opción: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        switch (option) {
            case 1:
                crearProfesor(session, scanner);
                break;
            case 2:
                actualizarProfesor(session, scanner);
                break;
            case 3:
                borrarProfesor(session, scanner);
                break;
            case 4:
                listarProfesores(session);
                break;
            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    private static void crearProfesor(Session session, Scanner scanner) {
        System.out.println("Ingrese los datos del profesor:");
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Fecha de Nacimiento (YYYY-MM-DD): ");
        String fechaNacimientoStr = scanner.nextLine();
        Date fechaNacimiento = java.sql.Date.valueOf(fechaNacimientoStr);

        System.out.print("Ciudad: ");
        String nombreCiudad = scanner.nextLine();
        Ciudad ciudad = obtenerCiudadPorNombre(session, nombreCiudad);
        if (ciudad == null) return;

        System.out.print("Antigüedad: ");
        int antiguedad = scanner.nextInt();
        scanner.nextLine();

        Profesor profesor = new Profesor(apellido, nombre, dni, fechaNacimiento, ciudad, antiguedad);
        Transaction tx = session.beginTransaction();
        try {
            profesorDAO.crearProfesor(session, profesor);
            tx.commit();
            System.out.println("Profesor creado con éxito.");
        } catch (Exception e) {
            tx.rollback();
            System.out.println("Error creando el profesor: " + e.getMessage());
        }
    }

    private static void actualizarProfesor(Session session, Scanner scanner) {
        listarProfesores(session);
        System.out.print("Ingrese el DNI del profesor a actualizar: ");
        String dni = scanner.nextLine();

        Profesor profesor = (Profesor) session.createQuery("FROM Profesor WHERE dni = :dni")
                                               .setParameter("dni", dni)
                                               .uniqueResult();
        if (profesor != null) {
            System.out.print("Nuevo apellido (actual: " + profesor.getApellido() + "): ");
            String nuevoApellido = scanner.nextLine();
            profesor.setApellido(nuevoApellido);

            Transaction tx = session.beginTransaction();
            try {
                session.update(profesor);
                tx.commit();
                System.out.println("Profesor actualizado con éxito.");
            } catch (Exception e) {
                tx.rollback();
                System.out.println("Error actualizando el profesor: " + e.getMessage());
            }
        } else {
            System.out.println("Profesor no encontrado.");
        }
    }

    private static void borrarProfesor(Session session, Scanner scanner) {
        System.out.print("Ingrese el DNI del profesor a borrar: ");
        String dni = scanner.nextLine();

        Profesor profesor = (Profesor) session.createQuery("FROM Profesor WHERE dni = :dni")
                                               .setParameter("dni", dni)
                                               .uniqueResult();

        if (profesor != null) {
            Transaction tx = session.beginTransaction();
            try {
                session.delete(profesor);
                tx.commit();
                System.out.println("Profesor borrado con éxito.");
            } catch (Exception e) {
                tx.rollback();
                System.out.println("Error borrando el profesor: " + e.getMessage());
            }
        } else {
            System.out.println("Profesor no encontrado.");
        }
    }

    private static void listarProfesores(Session session) {
        System.out.println("\n--- Lista de Profesores ---");
        profesorDAO.listarProfesores(session).forEach(profesor -> System.out.println("Profesor [ID=" + profesor.getId() + 
                                                                                     ", Apellido=" + profesor.getApellido() + 
                                                                                     ", Nombre=" + profesor.getNombre() + 
                                                                                     ", DNI=" + profesor.getDni() +
                                                                                     ", Fecha Nacimiento=" + profesor.getFechaNacimiento() + 
                                                                                     ", Ciudad=" + (profesor.getCiudad() != null ? profesor.getCiudad().getNombre() : "N/A") + 
                                                                                     ", Antigüedad=" + profesor.getAntiguedad() + "]"));
    }

    // CRUD Materias
    private static void crudMaterias(Session session, Scanner scanner) {
        System.out.println("\n===== CRUD Materias =====");
        System.out.println("1. Crear materia");
        System.out.println("2. Actualizar materia");
        System.out.println("3. Borrar materia");
        System.out.println("4. Listar todas las materias");
        System.out.print("Seleccione una opción: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        switch (option) {
            case 1:
                crearMateria(session, scanner);
                break;
            case 2:
                actualizarMateria(session, scanner);
                break;
            case 3:
                borrarMateria(session, scanner);
                break;
            case 4:
                listarMaterias(session);
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void crearMateria(Session session, Scanner scanner) {
        System.out.println("Ingrese los datos de la materia:");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Nivel: ");
        String nivel = scanner.nextLine();

        System.out.print("DNI del Profesor: ");
        String dniProfesor = scanner.nextLine();
        Profesor profesor = (Profesor) session.createQuery("FROM Profesor WHERE dni = :dni")
                                               .setParameter("dni", dniProfesor)
                                               .uniqueResult();
        if (profesor == null) {
            System.out.println("Profesor no encontrado. Primero cree el profesor.");
            return;
        }

        System.out.print("Nombre de la Carrera: ");
        String nombreCarrera = scanner.nextLine();
        Carrera carrera = (Carrera) session.createQuery("FROM Carrera WHERE nombre = :nombre")
                                           .setParameter("nombre", nombreCarrera)
                                           .uniqueResult();
        if (carrera == null) {
            System.out.println("Carrera no encontrada. Primero cree la carrera.");
            return;
        }

        Materia materia = new Materia(nombre, nivel, profesor, carrera);
        Transaction tx = session.beginTransaction();
        try {
            materiaDAO.crearMateria(session, materia);
            tx.commit();
            System.out.println("Materia creada con éxito.");
        } catch (Exception e) {
            tx.rollback();
            System.out.println("Error creando la materia: " + e.getMessage());
        }
    }

    private static void actualizarMateria(Session session, Scanner scanner) {
        listarMaterias(session);
        System.out.print("Ingrese el nombre de la materia a actualizar: ");
        String nombre = scanner.nextLine();

        Materia materia = (Materia) session.createQuery("FROM Materia WHERE nombre = :nombre")
                                           .setParameter("nombre", nombre)
                                           .uniqueResult();
        if (materia != null) {
            System.out.print("Nuevo nombre (actual: " + materia.getNombre() + "): ");
            String nuevoNombre = scanner.nextLine();
            materia.setNombre(nuevoNombre);

            Transaction tx = session.beginTransaction();
            try {
                session.update(materia);
                tx.commit();
                System.out.println("Materia actualizada con éxito.");
            } catch (Exception e) {
                tx.rollback();
                System.out.println("Error actualizando la materia: " + e.getMessage());
            }
        } else {
            System.out.println("Materia no encontrada.");
        }
    }

    private static void borrarMateria(Session session, Scanner scanner) {
        System.out.print("Ingrese el nombre de la materia a borrar: ");
        String nombre = scanner.nextLine();

        Materia materia = (Materia) session.createQuery("FROM Materia WHERE nombre = :nombre")
                                           .setParameter("nombre", nombre)
                                           .uniqueResult();
        if (materia != null) {
            Transaction tx = session.beginTransaction();
            try {
                session.delete(materia);
                tx.commit();
                System.out.println("Materia borrada con éxito.");
            } catch (Exception e) {
                tx.rollback();
                System.out.println("Error borrando la materia: " + e.getMessage());
            }
        } else {
            System.out.println("Materia no encontrada.");
        }
    }

    private static void listarMaterias(Session session) {
        System.out.println("\n--- Lista de Materias ---");
        materiaDAO.listarMaterias(session).forEach(System.out::println);
    }

    // CRUD Ciudad
    private static void crearCiudad(Session session, Scanner scanner) {
    System.out.print("Ingrese el nombre de la ciudad: ");
    String nombreCiudad = scanner.nextLine();

    // Verificar si la ciudad ya existe
    Ciudad ciudadExistente = (Ciudad) session.createQuery("FROM Ciudad WHERE nombre = :nombre")
                                             .setParameter("nombre", nombreCiudad)
                                             .uniqueResult();

    if (ciudadExistente != null) {
        System.out.println("La ciudad '" + nombreCiudad + "' ya existe en la base de datos.");
        return; // Salir del método si la ciudad ya existe
    }

    // Si la ciudad no existe, crear una nueva instancia y guardarla
    Ciudad nuevaCiudad = new Ciudad(nombreCiudad);
    Transaction tx = session.beginTransaction();
    try {
        session.save(nuevaCiudad);
        tx.commit();
        System.out.println("Ciudad creada con éxito.");
    } catch (Exception e) {
        tx.rollback();
        System.out.println("Error creando la ciudad: " + e.getMessage());
    }
}


    // CRUD Facultad
    private static void crearFacultad(Session session, Scanner scanner) {
    System.out.print("Ingrese el nombre de la facultad: ");
    String nombreFacultad = scanner.nextLine();
    System.out.print("Ingrese el nombre de la ciudad asociada: ");
    String nombreCiudad = scanner.nextLine();

    // Verificar si la facultad ya existe
    Facultad facultadExistente = (Facultad) session.createQuery("FROM Facultad WHERE nombre = :nombre")
                                                   .setParameter("nombre", nombreFacultad)
                                                   .uniqueResult();

    if (facultadExistente != null) {
        System.out.println("La facultad '" + nombreFacultad + "' ya existe en la base de datos.");
        return; // Salir del método si la facultad ya existe
    }

    // Verificar si la ciudad existe
    Ciudad ciudad = obtenerCiudadPorNombre(session, nombreCiudad);
    if (ciudad == null) return; // Si no existe la ciudad, salir del método

    // Crear y guardar la nueva facultad si no existe
    Facultad nuevaFacultad = new Facultad(nombreFacultad, ciudad);
    Transaction tx = session.beginTransaction();
    try {
        session.save(nuevaFacultad);
        tx.commit();
        System.out.println("Facultad creada con éxito.");
    } catch (Exception e) {
        tx.rollback();
        System.out.println("Error creando la facultad: " + e.getMessage());
    }
}


    // CRUD Carrera
    private static void crearCarrera(Session session, Scanner scanner) {
    System.out.print("Ingrese el nombre de la carrera: ");
    String nombreCarrera = scanner.nextLine();
    System.out.print("Ingrese el nombre de la facultad asociada: ");
    String nombreFacultad = scanner.nextLine();

    // Verificar si la carrera ya existe
    Carrera carreraExistente = (Carrera) session.createQuery("FROM Carrera WHERE nombre = :nombre")
                                                .setParameter("nombre", nombreCarrera)
                                                .uniqueResult();

    if (carreraExistente != null) {
        System.out.println("La carrera '" + nombreCarrera + "' ya existe en la base de datos.");
        return; // Salir del método si la carrera ya existe
    }

    // Verificar si la facultad existe
    Facultad facultad = (Facultad) session.createQuery("FROM Facultad WHERE nombre = :nombre")
                                          .setParameter("nombre", nombreFacultad)
                                          .uniqueResult();

    if (facultad == null) {
        System.out.println("Facultad no encontrada. Primero cree la facultad.");
        return;
    }

    // Crear y guardar la nueva carrera si no existe
    Carrera nuevaCarrera = new Carrera(nombreCarrera, facultad);
    Transaction tx = session.beginTransaction();
    try {
        session.save(nuevaCarrera);
        tx.commit();
        System.out.println("Carrera creada con éxito.");
    } catch (Exception e) {
        tx.rollback();
        System.out.println("Error creando la carrera: " + e.getMessage());
    }
}


    

private static Ciudad obtenerCiudadPorNombre(Session session, String nombreCiudad) {
    List<Ciudad> ciudades = session.createQuery("FROM Ciudad WHERE nombre = :nombre")
                                    .setParameter("nombre", nombreCiudad)
                                    .setMaxResults(1)  // Limita la consulta a un resultado
                                    .list();

    if (ciudades.isEmpty()) {
        System.out.println("Ciudad no encontrada. Primero cree la ciudad.");
        return null;
    }
    return ciudades.get(0);  // Devuelve la primera coincidencia
}


}

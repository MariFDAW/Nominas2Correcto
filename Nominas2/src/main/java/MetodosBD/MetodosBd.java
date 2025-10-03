package MetodosBD;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Laboral.DatosNoCorrectosException;
import Laboral.Empleado;
import Laboral.Nomina;

public class MetodosBd {
    /* Esta clase es donde se encuentran
     * todos los metodos para actualizar la BD
     */

    public void mostrarEmpleados() {
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT e.Nombre,e.DNI,e.SEXO,e.categoria,e.años,n.sueldo FROM empleados e JOIN nominas n ON e.DNI=n.empleado");) {

            while (rs.next()) {
                System.out.println(
                        "Nombre: " + rs.getString(1) + ", DNI: " + rs.getString(2) + ", Sexo: " + rs.getString(3)
                                + " ,Categoria: " + rs.getInt(4) + " ,Años: " + rs.getInt(5)+" ,Sueldo: "+rs.getDouble(6));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarSalarioEmpleado(String dni) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn
                     .prepareStatement("SELECT sueldo,empleado FROM nominas where empleado = ?");) {

            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Sueldo: " + rs.getDouble(1) + " ,Empleado: " + rs.getString(2));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Revisar
    public void actualizarEmpleados() {

        int opcion;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("MENÚ");
            System.out.println("0. Volver al inicio");
            System.out.println("1. Actualizar nombre");
            System.out.println("2. Actualizar dni");
            System.out.println("3. Actualizar sexo");
            System.out.println("4. Actualizar categoria");
            System.out.println("5. Actualizar años");

            System.out.println("------------------------------------------------------------------");
            System.out.println("Inserta un numero para elegir su opcion");
            opcion = sc.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo");
                    break;
                case 1:
                    System.out.println("Dime el dni del empleado a actualizar");
                    sc.nextLine();
                    String dni = sc.nextLine();
                    System.out.println("Dime el nombre nuevo del empleado a actualizar");
                    String nombre = sc.nextLine();
                    actualizaNombre(dni, nombre);
                    break;
                case 2:
                    System.out.println("Dime el dni del empleado a actualizar");
                    sc.nextLine();
                    String dniAnt = sc.nextLine();
                    System.out.println("Dime el dni nuevo del empleado a actualizar");
                    String dniNuv = sc.nextLine();
                    actualizaDni(dniAnt, dniNuv);
                    break;
                case 3:
                    dni = "";
                    System.out.println("Dime el dni del empleado a actualizar");
                    sc.nextLine();
                    dni = sc.nextLine();
                    System.out.println("Dime el sexo nuevo del empleado a actualizar");
                    String sexo = sc.nextLine();
                    actualizaSexo(dni, sexo);
                    break;
                case 4:
                    try {
                        dni = "";
                        Scanner scNum = new Scanner(System.in);
                        System.out.println("Dime el dni del empleado a actualizar");
                        sc.nextLine();
                        dni = sc.nextLine();
                        System.out.println("Dime la categoria nueva del empleado a actualizar");
                        int categoria = scNum.nextInt();
                        actualizaCategoria(dni, categoria);
                    } catch (DatosNoCorrectosException e) {
                        System.out.println("La categoria es entre 1 y 10");;
                    }
                    break;
                case 5:
                    try {
                        dni = "";
                        Scanner scNum2 = new Scanner(System.in);
                        System.out.println("Dime el dni del empleado a actualizar");
                        sc.nextLine();
                        dni = sc.nextLine();
                        System.out.println("Dime los años trabajados nuevos del empleado a actualizar");
                        int anyos = scNum2.nextInt();
                        actualizaAnyos(dni, anyos);
                    } catch (DatosNoCorrectosException e) {
                        System.out.println("Loa años deben ser positivos");;
                    }
                    break;

                default:
                    System.out.println("La opcion debe estar entre 0 y 5");
                    break;
            }
        } while (opcion != 0);
    }

    public void actualizarSueldoUnEmpleado(String dni) {
        int categoria = 0;
        int anyos = 0;
        Empleado e = null;
        Nomina n = new Nomina();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT categoria, años FROM empleados WHERE dni = ?")) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoria = rs.getInt("categoria");
                    anyos = rs.getInt("años");
                    e = new Empleado(" ", dni, ' ', categoria, anyos);
                }
            } catch (DatosNoCorrectosException ex) {
                throw new RuntimeException(ex);
            }
            //Compruebo que el empleado exista
            if (e != null) {
                String update = "UPDATE nominas SET sueldo = ? WHERE empleado = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                    ps2.setDouble(1, n.sueldo(e));
                    ps2.setString(2, dni);
                    ps2.executeUpdate();
                }
            } else {
                System.out.println("El empleado con dni "+dni+" no se encuentra en la BD");
            }

        } catch (Exception e2) {
            System.out.println( e2.getMessage());
        }
    }


    public void actualizarSueldosTodosEmpleados() throws DatosNoCorrectosException {
        String dni;
        int categoria, anyos;
        Empleado e;
        double sueldo;
        Nomina n = new Nomina();

        String select = "SELECT dni, categoria, años FROM empleados";
        String update = "UPDATE nominas SET sueldo = ? WHERE empleado = ?";

        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(select);
             PreparedStatement ps = conn.prepareStatement(update)) {

            while (rs.next()) {
                dni = rs.getString(1);
                categoria = rs.getInt(2);
                anyos = rs.getInt(3);

                // Crear empleado (nombre, dni, sexo, categoria, años)
                e = new Empleado("", dni, ' ', categoria, anyos);

                sueldo = n.sueldo(e);

                ps.setDouble(1, sueldo);
                ps.setString(2, dni);
                ps.executeUpdate();
            }

        } catch (SQLException  | DatosNoCorrectosException e2) {
            System.out.println("Error al actualizar sueldos: " + e2.getMessage());
        }
    }

    public void copiaBDEnFichero() {
        String nombre, dni;
        String sexo;
        int categoria, anyos;
        double sueldo;
        String ruta = "data\\copiaBD.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
             Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT e.Nombre, e.DNI, e.sexo, e.categoria, e.años, n.sueldo FROM empleados e JOIN nominas n ON e.dni = n.empleado");) {
            while (rs.next()) {
                nombre = rs.getString(1);
                dni = rs.getString(2);
                sexo = rs.getString(3);
                categoria = rs.getInt(4);
                anyos = rs.getInt(5);
                sueldo = rs.getDouble(6);
                bw.write("Nombre: " +nombre + ",");
                bw.write("Dni: " +dni + ",");
                bw.write("Sexo: " +sexo + ",");
                bw.write("Categoria: " +categoria + ",");
                bw.write("Años: " +anyos + ",");
                bw.write("Sueldo: " +sueldo + ".");
                bw.newLine();
            }

        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void actualizaNombre(String dni, String nombre) {
        String update = "UPDATE empleados SET Nombre = ? WHERE DNI = ?";
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             PreparedStatement ps = conn.prepareStatement(update)) {

            ps.setString(1, nombre);
            ps.setString(2, dni);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void actualizaDni(String dni, String dniNuevo) {
        String update = "UPDATE empleados SET DNI = ? WHERE DNI = ?";
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             PreparedStatement ps = conn.prepareStatement(update)) {

            ps.setString(1, dniNuevo);
            ps.setString(2, dni);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void actualizaSexo(String dni, String sexo) {
        String update = "UPDATE empleados SET SEXO = ? WHERE DNI = ?";
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             PreparedStatement ps = conn.prepareStatement(update)) {

            ps.setString(1, sexo);
            ps.setString(2, dni);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void actualizaCategoria(String dni, int categoria) throws DatosNoCorrectosException {
        if(categoria >= 1 && categoria <= 10) {

            String update = "UPDATE empleados SET categoria = ? WHERE DNI = ?";
            try (Connection conn = ConexionBD.getConnection();
                 Statement st = conn.createStatement();
                 PreparedStatement ps = conn.prepareStatement(update)) {

                ps.setInt(1, categoria);
                ps.setString(2, dni);

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            actualizarSueldoUnEmpleado(dni);
        }else{
            throw new DatosNoCorrectosException();
        }
    }

    public void actualizaAnyos(String dni, int anyos) throws DatosNoCorrectosException {
        if(anyos >0) {
            String update = "UPDATE empleados SET años = ? WHERE DNI = ?";
            try (Connection conn = ConexionBD.getConnection();
                 Statement st = conn.createStatement();
                 PreparedStatement ps = conn.prepareStatement(update)) {

                ps.setInt(1, anyos);
                ps.setString(2, dni);

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            actualizarSueldoUnEmpleado(dni);
        }else{
            throw new DatosNoCorrectosException();
        }
    }
}

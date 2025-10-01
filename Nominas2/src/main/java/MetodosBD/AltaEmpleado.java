package MetodosBD;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import Laboral.DatosNoCorrectosException;
import Laboral.Empleado;
import Laboral.Nomina;

public class AltaEmpleado {
    public void altaEmpleado(Empleado e) throws SQLException {
        Nomina n = new Nomina();
        String nombre = e.nombre;
        String dni = e.dni;
        char sexo = e.sexo;
        Integer categoria = e.getCategoria();
        Integer anyos = e.anyos;
        Double sueldo = n.sueldo(e);

        String insertEmpleados = "INSERT INTO Empleados (Nombre, DNI, SEXO, Categoria, Años) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertEmpleados);){

            ps.setString(1, nombre);
            ps.setString(2, dni);
            ps.setString(3, String.valueOf(sexo));
            ps.setInt(4, categoria);
            ps.setInt(5, anyos);

            ps.executeUpdate();
        }

        String insertNominas = "INSERT INTO nominas (sueldo,empleado) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertNominas);){

            ps.setDouble(1, sueldo);
            ps.setString(2, dni);

            ps.executeUpdate();
        }
    }

    public void altaEmpleados(){
        String insertEmpleados = "INSERT INTO empleados (nombre, dni, sexo, categoria, años) VALUES (?, ?, ?, ?, ?)";
        String insertNominas = "INSERT INTO nominas (sueldo,empleado) VALUES (?, ?)";
        String ruta = "data\\empleadosNuevos.txt";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertEmpleados);
             BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            String linea;

            while ((linea = br.readLine()) != null) {
                String[] cadena = linea.split(";");

                String nombre = cadena[0].trim();
                String dni = cadena[1].trim();
                char sexo = cadena[2].charAt(0);
                Integer categoria = Integer.parseInt(cadena[3]);
                Integer anyos = Integer.parseInt(cadena[4]);

                Empleado e = new Empleado(nombre, dni, sexo, categoria, anyos);

                ps.setString(1,e.nombre);
                ps.setString(2,e.dni);
                ps.setString(3,String.valueOf(e.sexo));
                ps.setInt(4,e.getCategoria());
                ps.setInt(5,e.anyos);
                ps.executeUpdate();
            try(PreparedStatement pst = conn.prepareStatement(insertNominas)){
                Nomina n = new Nomina();
                pst.setDouble(1,n.sueldo(e));
                pst.setString(2,e.dni);
                pst.executeUpdate();
                }
            }
        } catch (Exception | DatosNoCorrectosException e) {
            System.out.println(e.getMessage());
        }
    }
}

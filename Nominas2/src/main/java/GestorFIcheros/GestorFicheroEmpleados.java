package GestorFIcheros;
import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.IOException;
import java.util.*;

import Laboral.DatosNoCorrectosException;
import Laboral.Empleado;
import Laboral.Nomina;
public class GestorFicheroEmpleados {
    public List<Empleado> leerFichero() {

        List<Empleado> listaEmpleados = new ArrayList<>();
        String ruta = "data\\empleados.txt";
        String separador = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = "";
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] cadena = linea.split(separador);
                String nombre = cadena[0].trim();
                String dni = cadena[1].trim();
                char sexo = cadena[2].charAt(0);
                Integer categoria = Integer.parseInt(cadena[3]);
                Integer anyos = Integer.parseInt(cadena[4]);
                try {
                    Empleado e = new Empleado(nombre, dni, sexo, categoria, anyos);
                    listaEmpleados.add(e);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (IOException | DatosNoCorrectosException e) {
            System.out.println(e.getMessage());
        }
        return listaEmpleados;
    }

    public void escribeFichero(List<Empleado> listaEmpleado) {
        String ruta = "data\\salarios.dat";
        try (DataOutputStream dt = new DataOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < listaEmpleado.size(); i++) {
                Nomina n = new Nomina();
                dt.writeUTF(listaEmpleado.get(i).dni);
                dt.writeDouble(n.sueldo(listaEmpleado.get(i)));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

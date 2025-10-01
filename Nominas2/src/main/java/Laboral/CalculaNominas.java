package Laboral;
import java.util.List;
import java.util.Scanner;

import GestorFIcheros.GestorFicheroEmpleados;
import MetodosBD.AltaEmpleado;
import MetodosBD.MetodosBd;
public class CalculaNominas {

    /**
     * @param args
     *             Maria del carmen Farfán Gavilán
     *             Esto es para generar un javadoc
     */
    public static void main(String[] args) {
        MetodosBd mbd = new MetodosBd();

        AltaEmpleado altaBD = new AltaEmpleado();
        altaBD.altaEmpleados();

        List<Empleado> listaEmpleado = new GestorFicheroEmpleados().leerFichero();

        Empleado e1 = listaEmpleado.get(0);
        Empleado e2 = listaEmpleado.get(1);

        escribe(e1, e2);

        GestorFicheroEmpleados gt = new GestorFicheroEmpleados();
        gt.escribeFichero(listaEmpleado);

        int opcion;
        Scanner sc = new Scanner(System.in);
        do {

            System.out.println("MENÚ");
            System.out.println("0. SALIR");
            System.out.println("1. Mostrar info de la BD");
            System.out.println("2. Mostrar salario por dni de empleado");
            System.out.println("3. MENU de modificación de datos de los empleados");
            System.out.println("4. Recalcular y actualizar el sueldo de un empleado");
            System.out.println("5. Recalcular y actualizar los sueldos de todos los empleados");
            System.out.println("6. Copia de seguridad de la BD en fichero");

            System.out.println("------------------------------------------------------------------");
            System.out.println("Inserta un numero para elegir su opcion");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    mbd.mostrarEmpleados();
                    break;
                case 2:
                    System.out.println("Dime un dni");
                    sc.nextLine();
                    String dni = sc.nextLine();
                    mbd.mostrarSalarioEmpleado(dni);
                    break;
                case 3:
                    mbd.actualizarEmpleados();
                    break;
                case 4:
                    dni = "";
                    System.out.println("Dime un dni");
                    sc.nextLine();
                    dni = sc.nextLine();
                    mbd.actualizarSueldoUnEmpleado(dni);
                    break;
                case 5:
                    try {
                        mbd.actualizarSueldosTodosEmpleados();
                    } catch (DatosNoCorrectosException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    mbd.copiaBDEnFichero();
                    break;

                default:
                    System.out.println("La opcion debe estar entre 0 y 6");
                    break;
            }

        } while (opcion != 0);
        sc.close();
    }

    /**
     * @param e1 parametro del empleado 1
     * @param e2 parametro del empleado 2
     *           Para usar este metodo en el main, es necesario hacerlo static para
     *           permitir
     *           su uso en otros sitios
     *           Creo un objeto nomina para poder usar su metodo privado y asi
     *           obtener el sueldo
     */
    private static void escribe(Empleado e1, Empleado e2) {
        Nomina n = new Nomina();
        System.out.println("Empleado 1: Nombre: " + e1.nombre + " dni: " + e1.dni + " Sexo:" + e1.sexo
                + " Categoria: " + e1.getCategoria() + " Años: " + e1.anyos + " Sueldo: " + n.sueldo(e1));
        System.out.println("Empleado 2: Nombre: " + e2.nombre + " dni: " + e2.dni + " Sexo:" + e2.sexo
                + " Categoria: " + e2.getCategoria() + " Años: " + e2.anyos + " Sueldo: " + n.sueldo(e2));
    }

}

package Laboral;

public class Nomina {
    private static final int SUELDO_BASE[] = { 50000, 70000, 90000, 11000, 13000, 15000, 17000, 19000, 21000, 23000 };

    public double sueldo(Empleado e) {
        double sueldo = SUELDO_BASE[e.getCategoria()] + 5000 * e.anyos;
        return sueldo;
    }
}

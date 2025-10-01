package Laboral;

public class Empleado extends Persona {
    private int categoria;
    public int anyos;

    public Empleado(String nombre, String dni, char sexo, int categoria, int anyos) throws DatosNoCorrectosException {
        super(nombre, dni, sexo);
        if (categoria >= 1 && categoria <= 10) {
            this.categoria = categoria;
        } else {
            throw new DatosNoCorrectosException();
        }
        if (anyos > 0) {
            this.anyos = anyos;
        } else {
            throw new DatosNoCorrectosException();
        }
    }

    public Empleado(String nombre, String dni, char sexo) {
        super(nombre, dni, sexo);
        categoria = 1;
        anyos = 0;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getCategoria() {
        return categoria;
    }

    public void incrAnyo() {
        anyos++;
    }

    public void imprime() {
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + dni);
        System.out.println("Sexo: " + sexo);
        System.out.println("Categoria: " + categoria);
        System.out.println("Años: " + anyos);
    }
}

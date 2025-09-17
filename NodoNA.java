public class NodoNA {
    int id;
    String nombre;
    NodoNA izq;
    NodoNA der;

    public NodoNA(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.izq = null;
        this.der = null;
    }
}

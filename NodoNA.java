import java.util.ArrayList;
import java.util.List;

// Nodo n-ario básico (id entero + nombre simple para "empleado")
public class NodoNA {
    public int id;
    public String nombre;
    public List<NodoNA> hijos;

    public NodoNA(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.hijos = new ArrayList<>();
    }
}

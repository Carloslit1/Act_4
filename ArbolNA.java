import java.util.ArrayList;
import java.util.List;

public class ArbolNA {
    private NodoNA raiz;

    // ====== Inserción (recursiva) ======
    public void insertar(int id) {
        raiz = insertarRec(raiz, id);
    }

    private NodoNA insertarRec(NodoNA nodo, int id) {
        if (nodo == null) return new NodoNA(id);
        if (id < nodo.id) nodo.izquierdo = insertarRec(nodo.izquierdo, id);
        else if (id > nodo.id) nodo.derecho = insertarRec(nodo.derecho, id);
        // si es igual, no insertamos duplicado (o podrías manejar conteo)
        return nodo;
    }

    // ====== Búsqueda (recursiva) ======
    public boolean contiene(int id) {
        return contieneRec(raiz, id);
    }

    private boolean contieneRec(NodoNA nodo, int id) {
        if (nodo == null) return false;
        if (id == nodo.id) return true;
        if (id < nodo.id) return contieneRec(nodo.izquierdo, id);
        return contieneRec(nodo.derecho, id);
    }

    // ====== Eliminación (recursiva, casos 0/1/2 hijos) ======
    public void eliminar(int id) {
        raiz = eliminarRec(raiz, id);
    }

    private NodoNA eliminarRec(NodoNA nodo, int id) {
        if (nodo == null) return null;

        if (id < nodo.id) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, id);
        } else if (id > nodo.id) {
            nodo.derecho = eliminarRec(nodo.derecho, id);
        } else {
            // Encontrado: manejar casos
            // 1) sin hijos
            if (nodo.izquierdo == null && nodo.derecho == null) {
                return null;
            }
            // 2) un hijo
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;

            // 3) dos hijos: reemplazar por el mínimo del subárbol derecho
            NodoNA sucesor = minNodo(nodo.derecho);
            nodo.id = sucesor.id;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.id);
        }
        return nodo;
    }

    private NodoNA minNodo(NodoNA nodo) {
        // Recursivo: ir siempre al izquierdo
        if (nodo.izquierdo == null) return nodo;
        return minNodo(nodo.izquierdo);
    }

    // ====== Editar (actualizar ID): delete + insert ======
    public boolean editarId(int idActual, int idNuevo) {
        if (!contiene(idActual)) return false;
        if (contiene(idNuevo)) return false; // evitar duplicados
        eliminar(idActual);
        insertar(idNuevo);
        return true;
    }

    // ====== Recorridos (recursivos) ======
    public List<Integer> preorden() {
        List<Integer> res = new ArrayList<>();
        preordenRec(raiz, res);
        return res;
    }

    private void preordenRec(NodoNA nodo, List<Integer> res) {
        if (nodo == null) return;
        res.add(nodo.id);
        preordenRec(nodo.izquierdo, res);
        preordenRec(nodo.derecho, res);
    }

    public List<Integer> inorden() {
        List<Integer> res = new ArrayList<>();
        inordenRec(raiz, res);
        return res;
    }

    private void inordenRec(NodoNA nodo, List<Integer> res) {
        if (nodo == null) return;
        inordenRec(nodo.izquierdo, res);
        res.add(nodo.id);
        inordenRec(nodo.derecho, res);
    }

    public List<Integer> postorden() {
        List<Integer> res = new ArrayList<>();
        postordenRec(raiz, res);
        return res;
    }

    private void postordenRec(NodoNA nodo, List<Integer> res) {
        if (nodo == null) return;
        postordenRec(nodo.izquierdo, res);
        postordenRec(nodo.derecho, res);
        res.add(nodo.id);
    }

    // ====== Utilidades ======
    public void limpiar() { raiz = null; }

    public boolean vacio() { return raiz == null; }

    // Construir un BST balanceado desde un arreglo ordenado [lo..hi] (recursivo)
    public void construirBalanceadoDesdeOrdenado(int[] arr) {
        limpiar();
        raiz = construirBalanceadoRec(arr, 0, arr.length - 1);
    }

    private NodoNA construirBalanceadoRec(int[] arr, int lo, int hi) {
        if (lo > hi) return null;
        int mid = lo + (hi - lo) / 2;
        NodoNA nodo = new NodoNA(arr[mid]);
        nodo.izquierdo = construirBalanceadoRec(arr, lo, mid - 1);
        nodo.derecho  = construirBalanceadoRec(arr, mid + 1, hi);
        return nodo;
    }
}

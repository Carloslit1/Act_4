import java.util.*;

public class ArbolNA {
    private NodoNA raiz;

    // Crear raíz si no existe
    public void setRaiz(int id, String nombre) {
        if (raiz == null) raiz = new NodoNA(id, nombre);
    }

    public NodoNA getRaiz() { return raiz; }

    // Inserta hijo bajo parentId (DFS simple). Retorna true si insertó.
    public boolean insertar(int parentId, int id, String nombre) {
        if (raiz == null) return false;
        NodoNA p = buscar(parentId);
        if (p == null) return false;
        // Evitar ids duplicados (búsqueda rápida)
        if (buscar(id) != null) return false;
        p.hijos.add(new NodoNA(id, nombre));
        return true;
    }

    // Busca por id (DFS recursivo)
    public NodoNA buscar(int id) {
        return dfsBuscar(raiz, id);
    }

    private NodoNA dfsBuscar(NodoNA actual, int id) {
        if (actual == null) return null;
        if (actual.id == id) return actual;
        for (NodoNA h : actual.hijos) {
            NodoNA r = dfsBuscar(h, id);
            if (r != null) return r;
        }
        return null;
    }

    // Eliminar subárbol por id (no permite borrar la raíz en esta base)
    public boolean eliminar(int id) {
        if (raiz == null || raiz.id == id) return false;
        return eliminarRec(raiz, id);
    }

    private boolean eliminarRec(NodoNA actual, int id) {
        if (actual == null) return false;
        Iterator<NodoNA> it = actual.hijos.iterator();
        while (it.hasNext()) {
            NodoNA h = it.next();
            if (h.id == id) { it.remove(); return true; }
            if (eliminarRec(h, id)) return true;
        }
        return false;
    }

    // Recorridos
    public List<Integer> preorden() {
        List<Integer> res = new ArrayList<>();
        pre(raiz, res); return res;
    }
    private void pre(NodoNA n, List<Integer> res) {
        if (n == null) return;
        res.add(n.id);
        for (NodoNA h : n.hijos) pre(h, res);
    }

    public List<Integer> postorden() {
        List<Integer> res = new ArrayList<>();
        post(raiz, res); return res;
    }
    private void post(NodoNA n, List<Integer> res) {
        if (n == null) return;
        for (NodoNA h : n.hijos) post(h, res);
        res.add(n.id);
    }

    public List<Integer> porNiveles() {
        List<Integer> res = new ArrayList<>();
        if (raiz == null) return res;
        Queue<NodoNA> q = new ArrayDeque<>();
        q.add(raiz);
        while (!q.isEmpty()) {
            NodoNA u = q.remove();
            res.add(u.id);
            for (NodoNA h : u.hijos) q.add(h);
        }
        return res;
    }
}

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.*;

public class ArbolNA {
    NodoNA raiz;
    static List<Integer> arreglo = new ArrayList<>();
    static Logger LOG = Logger.getLogger("ActividadLogger");

    // ===== Insertar (recursivo)
    public void insertar(int id, String nombre) {
        raiz = insertarRec(raiz, id, nombre);
    }
    private NodoNA insertarRec(NodoNA n, int id, String nombre) {
        if (n == null) return new NodoNA(id, nombre);
        if (id < n.id) n.izq = insertarRec(n.izq, id, nombre);
        else if (id > n.id) n.der = insertarRec(n.der, id, nombre);
        else n.nombre = nombre; // si se repite ID, actualizamos nombre
        return n;
    }

    // ===== Buscar (recursivo)
    public NodoNA buscar(int id) {
        return buscarRec(raiz, id);
    }
    private NodoNA buscarRec(NodoNA n, int id) {
        if (n == null) return null;
        if (id == n.id) return n;
        return (id < n.id) ? buscarRec(n.izq, id) : buscarRec(n.der, id);
    }

    // ===== Eliminar (recursivo)
    public void eliminar(int id) {
        raiz = eliminarRec(raiz, id);
    }
    private NodoNA eliminarRec(NodoNA n, int id) {
        if (n == null) return null;
        if (id < n.id) n.izq = eliminarRec(n.izq, id);
        else if (id > n.id) n.der = eliminarRec(n.der, id);
        else {
            if (n.izq == null) return n.der;
            if (n.der == null) return n.izq;
            NodoNA s = min(n.der);
            n.id = s.id; n.nombre = s.nombre;
            n.der = eliminarRec(n.der, s.id);
        }
        return n;
    }
    private NodoNA min(NodoNA n) {
        return (n.izq == null) ? n : min(n.izq);
    }

    // ===== Recorridos (recursivos)
    public void preorden(NodoNA n) {
        if (n != null) {
            System.out.print(n.id + ":" + n.nombre + " ");
            preorden(n.izq);
            preorden(n.der);
        }
    }
    public void inorden(NodoNA n) {
        if (n != null) {
            inorden(n.izq);
            System.out.print(n.id + ":" + n.nombre + " ");
            inorden(n.der);
        }
    }
    public void postorden(NodoNA n) {
        if (n != null) {
            postorden(n.izq);
            postorden(n.der);
            System.out.print(n.id + ":" + n.nombre + " ");
        }
    }

    // ===== Cargar 1000 empleados fijos
    public void cargarEmpleados() {
        raiz = null;
        arreglo.clear();
        for (int i = 1; i <= 1000; i++) {
            String nombre = String.format("Empleado%04d", i);
            insertar(i, nombre);
            arreglo.add(i);
        }
        LOG.info("Cargados 1000 empleados (IDs 1..1000).");
    }

    // ===== Utilidad: búsqueda secuencial en lista
    private static boolean busquedaSecuencial(List<Integer> lista, int objetivo) {
        for (int x : lista) {
            if (x == objetivo) return true;
        }
        return false;
    }

    // ===== Main con menú
    public static void main(String[] args) {
        configurarLogger();
        ArbolNA arbol = new ArbolNA();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        arbol.cargarEmpleados(); // Ya inicia con 1000 empleados cargados

        while (!salir) {
            System.out.println("\n===== MENÚ EMPLEADOS (Árbol Binario) =====");
            System.out.println("1) Mostrar recorridos");
            System.out.println("2) Buscar empleado por ID");
            System.out.println("3) Eliminar empleado");
            System.out.println("4) Comparar búsqueda (Arreglo vs Árbol)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1" -> {
                    System.out.println("\nRecorrido INORDEN (primeros 30):");
                    arbol.inorden(arbol.raiz);
                    System.out.println();
                    System.out.println("\nRecorrido PREORDEN (primeros 30):");
                    arbol.preorden(arbol.raiz);
                    System.out.println();
                    System.out.println("\nRecorrido POSTORDEN (primeros 30):");
                    arbol.postorden(arbol.raiz);
                    System.out.println();
                    LOG.info("Mostrados recorridos de ejemplo.");
                }
                case "2" -> {
                    System.out.print("ID a buscar: ");
                    int id = leerEntero(sc);
                    long t0 = System.nanoTime();
                    NodoNA n = arbol.buscar(id);
                    long t1 = System.nanoTime();
                    if (n != null) {
                        System.out.println("Encontrado: " + n.id + " - " + n.nombre + " (" + ms(t0, t1) + " ms)");
                        LOG.info("Busqueda exitosa ID=" + id);
                    } else {
                        System.out.println("No encontrado (" + ms(t0, t1) + " ms)");
                        LOG.info("Busqueda fallida ID=" + id);
                    }
                }
                case "3" -> {
                    System.out.print("ID a eliminar: ");
                    int id = leerEntero(sc);
                    NodoNA n = arbol.buscar(id);
                    arbol.eliminar(id);
                    arreglo.remove(Integer.valueOf(id));
                    if (n != null) {
                        System.out.println("Eliminado: " + id + " - " + n.nombre);
                        LOG.info("Empleado eliminado ID=" + id);
                    } else {
                        System.out.println("No existía ese ID.");
                        LOG.info("Intento eliminar ID=" + id + " (no existe)");
                    }
                }
                case "4" -> {
                    System.out.print("ID a comparar en búsqueda: ");
                    int objetivo = leerEntero(sc);
                    final int rep = 10000;

                    long a0 = System.nanoTime();
                    boolean okA = false;
                    for (int i = 0; i < rep; i++) okA = busquedaSecuencial(arreglo, objetivo);
                    long a1 = System.nanoTime();

                    long b0 = System.nanoTime();
                    boolean okB = false;
                    for (int i = 0; i < rep; i++) okB = (arbol.buscar(objetivo) != null);
                    long b1 = System.nanoTime();

                    double msA = ms(a0, a1);
                    double msB = ms(b0, b1);

                    System.out.println("Arreglo (lineal)  ~ " + String.format("%.3f ms", msA));
                    System.out.println("Árbol   (BST)     ~ " + String.format("%.3f ms", msB));
                    LOG.info("Comparación búsqueda ID=" + objetivo + " -> Arreglo=" + msA + "ms, Árbol=" + msB + "ms");
                }
                case "0" -> salir = true;
                default -> System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }

    // ===== Helpers
    private static void configurarLogger() {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.INFO);
        try {
            File dir = new File("logs");
            if (!dir.exists()) dir.mkdirs();
            FileHandler fh = new FileHandler("logs/actividad.log", true);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.INFO);
            LOG.addHandler(fh);
        } catch (IOException e) {
            System.out.println("No se pudo crear el log: " + e.getMessage());
        }
    }
    private static int leerEntero(Scanner sc) {
        while (true) {
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (Exception e) { System.out.print("Número válido: "); }
        }
    }
    private static double ms(long t0, long t1) {
        return (t1 - t0) / 1_000_000.0;
    }
}

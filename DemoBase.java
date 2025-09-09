import java.util.*;

public class DemoBase {

    private static final Scanner sc = new Scanner(System.in);

    // Representación "arreglo" para comparación (usamos ArrayList para facilitar A/B/M, pero la búsqueda es secuencial O(n))
    private static final List<Integer> empleadosArreglo = new ArrayList<>();

    // Árbol binario (BST) recursivo
    private static final ArbolNA empleadosArbol = new ArbolNA();

    public static void main(String[] args) {
        boolean salir = false;
        while (!salir) {
            menu();
            int op = leerEntero("Elige una opción: ");
            try {
                switch (op) {
                    case 1 -> cargarPrimeros100();
                    case 2 -> mostrarRecorridos();
                    case 3 -> agregarEmpleado();
                    case 4 -> eliminarEmpleado();
                    case 5 -> editarEmpleado();
                    case 6 -> buscarConComparativa();
                    case 7 -> benchmarkMasivo();
                    case 0 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("¡Hasta luego!");
    }

    private static void menu() {
        System.out.println("=======================================");
        System.out.println("  Gestión de Empleados con Árbol (BST)");
        System.out.println("=======================================");
        System.out.println("1) Cargar empleados 1..100 (y construir BST balanceado)");
        System.out.println("2) Mostrar recorridos (Preorden / Inorden / Postorden)");
        System.out.println("3) Agregar empleado (ID entero)");
        System.out.println("4) Eliminar empleado (ID entero)");
        System.out.println("5) Editar empleado (ID_actual -> ID_nuevo)");
        System.out.println("6) Buscar empleado y comparar tiempos (Arreglo vs Árbol)");
        System.out.println("7) Benchmark masivo (1000 búsquedas aleatorias)");
        System.out.println("0) Salir");
    }

    // ======= 1) Cargar 1..100 =======
    private static void cargarPrimeros100() {
        empleadosArreglo.clear();
        for (int i = 1; i <= 100; i++) {
            empleadosArreglo.add(i);
        }
        // construir BST balanceado desde array ordenado (recursivo)
        int[] base = empleadosArreglo.stream().mapToInt(Integer::intValue).toArray();
        empleadosArbol.construirBalanceadoDesdeOrdenado(base);

        System.out.println("Se cargaron empleados 1..100 en arreglo y árbol balanceado.");
        System.out.println("Total en arreglo: " + empleadosArreglo.size());
        System.out.println("Total en árbol:   " + empleadosArbol.inorden().size());
    }

    // ======= 2) Recorridos =======
    private static void mostrarRecorridos() {
        if (empleadosArreglo.isEmpty()) {
            System.out.println("Primero carga empleados (opción 1).");
            return;
        }
        System.out.println("Preorden : " + empleadosArbol.preorden());
        System.out.println("Inorden  : " + empleadosArbol.inorden());
        System.out.println("Postorden: " + empleadosArbol.postorden());
    }

    // ======= 3) Agregar =======
    private static void agregarEmpleado() {
        int id = leerEntero("ID a agregar: ");
        if (id <= 0) { System.out.println("El ID debe ser positivo."); return; }

        if (empleadosArreglo.contains(id)) {
            System.out.println("El ID ya existe en el arreglo/árbol.");
            return;
        }
        empleadosArreglo.add(id); // simulamos arreglo, sin ordenar a propósito (la búsqueda seguirá siendo O(n))
        empleadosArbol.insertar(id);
        System.out.println("Agregado ID " + id + " en arreglo y árbol.");
    }

    // ======= 4) Eliminar =======
    private static void eliminarEmpleado() {
        int id = leerEntero("ID a eliminar: ");
        boolean rem = empleadosArreglo.remove(Integer.valueOf(id));
        empleadosArbol.eliminar(id);
        if (rem) System.out.println("Eliminado ID " + id + ".");
        else System.out.println("ID no encontrado en arreglo (y retirado del árbol si existía).");
    }

    // ======= 5) Editar =======
    private static void editarEmpleado() {
        int actual = leerEntero("ID actual: ");
        int nuevo  = leerEntero("ID nuevo: ");
        if (nuevo <= 0) { System.out.println("El ID nuevo debe ser positivo."); return; }
        if (!empleadosArreglo.contains(actual)) {
            System.out.println("El ID actual no existe.");
            return;
        }
        if (empleadosArreglo.contains(nuevo)) {
            System.out.println("El ID nuevo ya existe.");
            return;
        }
        // arreglo (secuencial)
        empleadosArreglo.remove(Integer.valueOf(actual));
        empleadosArreglo.add(nuevo);
        // árbol
        boolean ok = empleadosArbol.editarId(actual, nuevo);
        System.out.println(ok ? ("Editado: " + actual + " -> " + nuevo)
                              : "No se pudo editar en el árbol (¿ID inexistente o duplicado?).");
    }

    // ======= 6) Buscar + Comparativa =======
    private static void buscarConComparativa() {
        if (empleadosArreglo.isEmpty()) {
            System.out.println("Primero carga empleados (opción 1).");
            return;
        }
        int objetivo = leerEntero("ID a buscar: ");
        final int repeticiones = 50_000; // repetir muchas veces para medir mejor

        // Búsqueda secuencial en arreglo (O(n))
        long t1 = System.nanoTime();
        boolean foundArr = false;
        for (int i = 0; i < repeticiones; i++) {
            foundArr = busquedaSecuencial(empleadosArreglo, objetivo);
        }
        long t2 = System.nanoTime();

        // Búsqueda recursiva en árbol (O(h))
        long t3 = System.nanoTime();
        boolean foundTree = false;
        for (int i = 0; i < repeticiones; i++) {
            foundTree = empleadosArbol.contiene(objetivo);
        }
        long t4 = System.nanoTime();

        double msArreglo = (t2 - t1) / 1_000_000.0;
        double msArbol   = (t4 - t3) / 1_000_000.0;

        System.out.println("Resultado arreglo : " + (foundArr ? "ENCONTRADO" : "NO") +
                           " | tiempo ~ " + String.format("%.3f ms", msArreglo));
        System.out.println("Resultado árbol   : " + (foundTree ? "ENCONTRADO" : "NO") +
                           " | tiempo ~ " + String.format("%.3f ms", msArbol));
        System.out.println("**Nota:** típicamente el árbol es mucho más rápido que la búsqueda secuencial,\n" +
                           "sobre todo con más datos, porque su profundidad crece ~log(n) cuando está balanceado.");
    }

    // ======= 7) Benchmark masivo (opcional) =======
    private static void benchmarkMasivo() {
        if (empleadosArreglo.isEmpty()) {
            System.out.println("Primero carga empleados (opción 1).");
            return;
        }
        Random rnd = new Random();
        int pruebas = 1000;
        long tArrIni = System.nanoTime();
        for (int i = 0; i < pruebas; i++) {
            int objetivo = rnd.nextInt(150) + 1; // puede/no existir
            busquedaSecuencial(empleadosArreglo, objetivo);
        }
        long tArrFin = System.nanoTime();

        long tTreeIni = System.nanoTime();
        for (int i = 0; i < pruebas; i++) {
            int objetivo = rnd.nextInt(150) + 1;
            empleadosArbol.contiene(objetivo);
        }
        long tTreeFin = System.nanoTime();

        double msArr = (tArrFin - tArrIni) / 1_000_000.0;
        double msTree = (tTreeFin - tTreeIni) / 1_000_000.0;

        System.out.println("Benchmark 1000 búsquedas aleatorias:");
        System.out.println("Secuencial (arreglo): " + String.format("%.3f ms", msArr));
        System.out.println("BST recursivo (árbol): " + String.format("%.3f ms", msTree));
    }

    // ======= Utilidades =======
    private static boolean busquedaSecuencial(List<Integer> lista, int objetivo) {
        // Búsqueda lineal O(n)
        for (int x : lista) if (x == objetivo) return true;
        return false;
    }

    private static int leerEntero(String msg) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            System.out.print("Ingresa un número entero: ");
            sc.next();
        }
        return sc.nextInt();
    }
}

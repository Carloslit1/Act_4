import java.io.*;
import java.util.*;
import java.util.logging.*;

public class DemoBase {
    static List<Integer> arreglo = new ArrayList<>();
    static Logger LOG = Logger.getLogger("ActividadLogger");

    public static void main(String[] args) {
        configurarLogger();
        ArbolNA arbol = new ArbolNA();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        // Cargar 1000 empleados
        for (int i = 1; i <= 1000; i++) {
            String nombre = String.format("Empleado%04d", i);
            arbol.insertar(i, nombre);
            arreglo.add(i);
        }
        LOG.info("Se cargaron 1000 empleados (IDs 1..1000)");

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
                    System.out.println("\nRecorrido INORDEN:");
                    arbol.inorden(arbol.raiz);
                    System.out.println();
                    System.out.println("\nRecorrido PREORDEN:");
                    arbol.preorden(arbol.raiz);
                    System.out.println();
                    System.out.println("\nRecorrido POSTORDEN:");
                    arbol.postorden(arbol.raiz);
                    System.out.println();
                    LOG.info("Mostrados los recorridos del árbol.");
                }
                case "2" -> {
                    System.out.print("ID a buscar: ");
                    int id = leerEntero(sc);
                    long t0 = System.nanoTime();
                    NodoNA n = arbol.buscar(id);
                    long t1 = System.nanoTime();
                    if (n != null) {
                        System.out.println("Encontrado: " + n.id + " - " + n.nombre + " (" + ms(t0, t1) + " ms)");
                        LOG.info("Búsqueda exitosa ID=" + id);
                    } else {
                        System.out.println("No encontrado (" + ms(t0, t1) + " ms)");
                        LOG.info("Búsqueda fallida ID=" + id);
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
                        LOG.info("Intento de eliminar ID=" + id + " (no existe)");
                    }
                }
                case "4" -> {
                    System.out.print("ID a comparar: ");
                    int objetivo = leerEntero(sc);
                    final int rep = 5000;

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

                    System.out.println("Búsqueda secuencial (arreglo): " + String.format("%.3f ms", msA));
                    System.out.println("Búsqueda en árbol binario: " + String.format("%.3f ms", msB));
                    LOG.info("Comparación búsqueda ID=" + objetivo + 
                             " -> Arreglo=" + msA + "ms, Árbol=" + msB + "ms");
                }
                case "0" -> {
                    salir = true;
                    LOG.info("Programa finalizado por el usuario.");
                }
                default -> System.out.println("Opción no válida.");
            }
        }
        sc.close();
    }

    // Búsqueda secuencial en arreglo
    private static boolean busquedaSecuencial(List<Integer> lista, int objetivo) {
        for (int x : lista) {
            if (x == objetivo) return true;
        }
        return false;
    }

    // Logger
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

    // Calcular milisegundos
    private static double ms(long t0, long t1) {
        return (t1 - t0) / 1_000_000.0;
    }

    // Leer enteros de consola
    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Número válido: ");
            }
        }
    }
}

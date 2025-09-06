public class DemoBase {
    // Main de prueba: arma un arbolito, muestra recorridos y hace operaciones.
    public static void main(String[] args) {
        ArbolNA t = new ArbolNA();

        // "Gestión de empleados" muy básica: id = clave, nombre = etiqueta
        t.setRaiz(100, "CEO");                    // raíz
        // Insertar bajo 100
        t.insertar(100, 110, "Ventas");
        t.insertar(100, 120, "Ingenieria");
        t.insertar(100, 130, "Operaciones");
        // Insertar bajo 120
        t.insertar(120, 121, "Backend");
        t.insertar(120, 122, "Frontend");
        // Insertar bajo 110
        t.insertar(110, 111, "Regional Norte");

        // Búsqueda
        System.out.println("Buscar 122: " + (t.buscar(122) != null));

        // Recorridos
        System.out.println("Preorden:    " + t.preorden());
        System.out.println("Postorden:   " + t.postorden());
        System.out.println("Por niveles: " + t.porNiveles());

        // Eliminar subárbol
        System.out.println("Eliminar 121: " + t.eliminar(121));
        System.out.println("Preorden (luego de eliminar 121): " + t.preorden());

        // Nota: esta es solo la base. Luego puedes:
        // - Validar que no haya ciclos (en n-ario normalmente controlas al insertar).
        // - Prohibir ids duplicados (ya se revisa en insertar()).
        // - Agregar métricas: altura, tamaño, etc.
        // - Comparar búsqueda DFS vs. “búsqueda secuencial” en una lista por performance.
    }
}

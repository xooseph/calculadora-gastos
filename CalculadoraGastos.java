import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CalculadoraGastos {
    // Ahora se utilizan ArrayList en vez de arrays
    private ArrayList<Movimiento> todosLosMovimientos;
    private HashMap<String, Gasto> gastosMap; // Para buscar rápido

    private double presupuesto;
    private Scanner scanner;


    public CalculadoraGastos() {
        todosLosMovimientos = new ArrayList<>();
        gastosMap = new HashMap<>();
        presupuesto = 0.0;
        scanner = new Scanner(System.in);
    }

    public void empezar() {
        System.out.println("========================================");
        System.out.println("    CALCULADORA DE GASTOS PERSONALES    ");
        System.out.println("========================================");

        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcion(scanner);

            switch (opcion) {
                case 1:
                    agregarGasto();
                    break;
                case 2:
                    registrarIngreso();
                    break;
                case 3:
                    verTodo();
                    break;
                case 4:
                    verSoloGastos();
                    break;
                case 5:
                    verPorCategoria();
                    break;
                case 6:
                    buscarPorGasto();
                    break;
                case 7:
                    verResumen();
                    break;
                case 8:
                    System.out.println("\n¡Gracias por usar la calculadora!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 8);

        scanner.close();
    }

    public static void main (String[] args) {
        CalculadoraGastos calc = new CalculadoraGastos();
        calc.empezar();
    }

    public void mostrarMenu() {
        System.out.println("--- MENÚ PRINCIPAL ---");
        System.out.println("1. Agregar un gasto");
        System.out.println("2. Registrar un ingreso");
        System.out.println("3. Ver todo");
        System.out.println("4. Ver solo gastos");
        System.out.println("5. Ver por categoría");
        System.out.println("6. Buscar un gasto");
        System.out.println("7. Ver resumen");
        System.out.println("8. Salir");
        System.out.println("Presupuesto disponible: $" + presupuesto);
        System.out.print("Elige una opción: ");
    }

    public int leerOpcion(Scanner scanner) {
        int opcion = 0;

        while (opcion < 1 || opcion > 8) {
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();

                if (opcion < 1 || opcion > 8) {
                    System.out.print("Debe ser entre 1 y 8, intenta de "
                            + "nuevo: ");
                }
            } else {
                System.out.print("Eso no es un número, intenta de nuevo: ");
                scanner.next();
            }
        }

        return opcion;
    }

    private void agregarGasto() {
        scanner.nextLine(); // Limpiar

        System.out.println("--- AGREGAR GASTO ---");
        System.out.print("¿En qué gastaste? ");
        String descripcion = scanner.nextLine();

        // Validar qué escribió algo
        while (descripcion.trim().isEmpty()) {
            System.out.print("Necesitas escribir algo, ¿en qué gastaste? ");
            descripcion = scanner.nextLine();
        }

        // Leer cuánto gastó
        System.out.print("¿Cuánto gastaste? $");
        double monto = leerMonto();

        // Elegir categoría
        System.out.println("Categorías:");
        System.out.println("1. Comida");
        System.out.println("2. Transporte");
        System.out.println("3. Entretenimiento");
        System.out.println("4. Salud");
        System.out.println("5. Otros");
        System.out.print("¿En qué categoría va? ");

        int cat = leerCategoriaValida();
        String categoria = "";

        if (cat == 1 ) {
            categoria = "Comida";
        } else if (cat == 2) {
            categoria = "Transporte";
        } else if (cat == 3) {
            categoria = "Entretenimiento";
        } else if (cat == 4) {
            categoria = "Salud";
        } else {
            categoria = "Otros";
        }

        // Creo el objeto Gasto
        Gasto nuevoGasto = new Gasto(descripcion, monto, categoria);

        // Lo agrego al ArrayList
        todosLosMovimientos.add(nuevoGasto);

        // Y también al HashMap para buscarlo después
        gastosMap.put(descripcion.toLowerCase(), nuevoGasto);

        presupuesto = presupuesto - monto;

        System.out.println("¡Listo! Gasto registrado.");
        System.out.println("Gastaste $" + monto);
        System.out.println("Te quedan $" + presupuesto);

        if (presupuesto < 0) {
            System.out.println("¡Cuidado! Ya estás en nagativo.");
        }
    }

    private void registrarIngreso() {
        scanner.nextLine();

        System.out.println("--- NUEVO INGRESO ---");
        System.out.print("¿De dónde vino el dinero? ");
        String descripcion = scanner.nextLine();

        while (descripcion.trim().isEmpty()) {
            System.out.print("Necesitas escribir algo: ");
            descripcion = scanner.nextLine();
        }

        System.out.print("¿Cuánto ingresaste? $");
        double monto = leerMonto();

        // Creo el objeto Ingreso
        Ingreso nuevoIngreso = new Ingreso(descripcion, monto);
        todosLosMovimientos.add(nuevoIngreso);

        presupuesto = presupuesto + monto;

        System.out.println("¡Listo! Ingreso registrado.");
        System.out.println("Ingresaste: $" + monto);
        System.out.println("Ahora tienes: $" + presupuesto);
    }

    private void verTodo() {
        System.out.println("--- TODOS LOS MOVIMIENTOS ---");

        if (todosLosMovimientos.isEmpty()) {
            System.out.println("No hay nada registrado todavía.");

            return;
        }

        // Aca el polimorfismo de cada objeto se muestra diferente
        for (Movimiento m : todosLosMovimientos) {
            m.mostrar();
        }

        System.out.println("Total: " + todosLosMovimientos.size() + " "
                + "movimientos");
    }

    private void verSoloGastos() {
        System.out.println("--- MIS GASTOS ---");

        int contador = 0;

        // Filtro solo los gastos usando instanceof
        for (Movimiento m: todosLosMovimientos) {
            if (m instanceof Gasto) {
                m.mostrar();
                contador++;
            }
        }

        if (contador == 0) {
            System.out.println("No hay gastos todavía.");
        } else {
            System.out.println("Total: " + contador + " gastos");
        }
    }

    private void verPorCategoria() {
        if (todosLosMovimientos.isEmpty()) {
            System.out.println("No hay nada registrado todavía.");

            return;
        }

        System.out.println("--- FILTRAR POR CATEGORÍA ---");
        System.out.println("1. Comida");
        System.out.println("2. Transporte");
        System.out.println("3. Entretenimiento");
        System.out.println("4. Salud");
        System.out.println("5. Otros");
        System.out.print("¿Cuál quieres ver? ");

        int cat = leerCategoriaValida();
        String categoriaElegida = "";

        if (cat == 1) categoriaElegida = "Comida";
        else if (cat == 2) categoriaElegida = "Transporte";
        else if (cat == 3) categoriaElegida = "Entretenimiento";
        else if (cat == 4) categoriaElegida = "Salud";
        else categoriaElegida = "Otros";

        System.out.println("Gastos en: " + categoriaElegida);

        double total = 0;
        int contador = 0;

        for (Movimiento m : todosLosMovimientos) {
            if (m instanceof Gasto) {
                Gasto g = (Gasto) m;
                if (g.getCategoria().equals(categoriaElegida)) {
                    contador++;
                    System.out.println(contador + ". " + g.getDescripcion()
                            + " - $" + g.getMonto());
                    total = total + g.getMonto();
                }
            }
        }

        if (contador == 0) {
            System.out.println("No hay gastos en esta categoría.");
        } else {
            System.out.println("Total en " + categoriaElegida + ": $" + total);
        }
    }

    private void buscarPorGasto() {
        scanner.nextLine();

        System.out.println("--- BUSCAR GASTO ---");
        System.out.print("¿Qué gasto quiere buscar? ");

        String buscar = scanner.nextLine().toLowerCase();

        // Acá usó el HashMap para buscar rápido
        Gasto encontrado = gastosMap.get(buscar);

        if (encontrado != null) {
            System.out.println("Encontrado:");
            encontrado.mostrar();
        } else {
            System.out.println("No se encontró ese gasto.");
        }
    }

    private double leerMonto() {
        double monto = -1;

        while (monto <= 0) {
            if (scanner.hasNextDouble()) {
                monto = scanner.nextDouble();

                if (monto <= 0) {
                    System.out.print("Debe ser mayor que cero. Intenta de "
                            + "nuevo: $");
                }
            } else {
                System.out.print("Eso no es un número válido. Intenta de "
                        + "nuevo: $");
                scanner.next();
            }
        }

        return monto;
    }

    private int leerCategoriaValida() {
        int opcion = 0;

        while (opcion < 1 || opcion > 5) {
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();

                if (opcion < 1 || opcion > 5) {
                    System.out.print("Esa categoría no es válida. Debe ser "
                            + "entre 1 y 5: ");
                }
            } else {
                System.out.print("Eso no es un número: ");
                scanner.next();
            }
        }

        return opcion;
    }



    private void verResumen() {
        System.out.println("========================================");
        System.out.println("          RESUMEN FINANCIERO            ");
        System.out.println("========================================");

        if (todosLosMovimientos.isEmpty()) {
            System.out.println("No hay nada que mostrar.");

            return;
        }

        double totalGastos = 0;
        double totalIngresos = 0;
        int cantGastos = 0;
        int cantIngresos = 0;

        // Separo gastos de ingresos
        for (Movimiento m : todosLosMovimientos) {
            if (m instanceof Gasto) {
                totalGastos = totalGastos + m.getMonto();
                cantGastos++;
            } else if (m instanceof Ingreso) {
                totalIngresos = totalIngresos + m.getMonto();
                cantIngresos++;
            }
        }

        System.out.println("Ingresos: $" + totalIngresos + " ("
                + cantIngresos + ")");
        System.out.println("Gastos: $" + totalGastos + " (" + cantGastos + ")");
        System.out.println("Balance: $" + (totalIngresos - totalGastos));
        System.out.println("Disponible ahora: $" + presupuesto);

        if (cantGastos > 0) {
            double promedio = totalGastos / cantGastos;
            System.out.println("Promedio por gasto: $" + promedio);
        }

        // Muestro por categoria
        System.out.println("Por categoría:");
        mostrarTotalesPorCategoria();
    }

    private void mostrarTotalesPorCategoria() {
        String[] cats = {"Comida", "Transporte", "Entretenimiento", "Salud",
                "Otros"};

        for (String cat : cats) {
            double total = 0;
            int cantidad = 0;

            for (Movimiento m : todosLosMovimientos) {
                if (m instanceof Gasto) {
                    Gasto g = (Gasto) m;

                    if (g.getCategoria().equals(cat)) {
                        total = total + g.getMonto();
                        cantidad++;
                    }
                }
            }

            if (cantidad > 0) {
                System.out.println(" " + cat + ": $" + total + " (" + cantidad
                        + ")");
            }
        }
    }
}

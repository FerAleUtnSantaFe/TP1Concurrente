import estrategia.EstrategiaGranularidadFina;
import estrategia.EstrategiaSincronizacionOptimista;
import estrategia.EstrategiaNoBloqueante;
import estrategia.estrategia;
import evaluacion.Escenario;

import java.util.Arrays;

public class App {
    private static final int REPETICIONES = 5;
    private static final int OPERACIONES_TOTALES = 100000; // Fijo para escenario 2
    private static final int OPERACIONES_POR_HILO = 10000; // Para escenario 3
    private static final int[] TOTAL_HILOS = {4, 8, 16, 32};
    private static final double[][] PROPORCIONES = {
            {0.5, 0.25, 0.25}, // 50% add, 25% remove, 25% contains
            {0.33, 0.33, 0.34}, // 33% de cada operación
            {0.10, 0.45, 0.45}, // 10% add, 45% remove, 45% contains
    };

    public static void main(String[] args) {
        estrategia[] estrategias = {
                new EstrategiaGranularidadFina(),
                new EstrategiaSincronizacionOptimista(),
                new EstrategiaNoBloqueante()
        };

        for (estrategia estrategiaLista : estrategias) {
            System.out.println("\n=== Evaluando estrategia: " + estrategiaLista.name() + " ===");

            // Escenario 1: La variación en la proporción de hilos que ejecutan determinadas operaciones
            // manteniendo constante la cantidad de hilos totales. 
            for (int hilos : TOTAL_HILOS) {
                for (double[] proporcion : PROPORCIONES) {
                    ejecutarEscenario(estrategiaLista, hilos, OPERACIONES_POR_HILO, proporcion, "Escenario 1");
                }
            }

            // Escenario 2: La variación de la cantidad de hilos totales, si se preserva el número total de operaciones.
            for (int hilos : TOTAL_HILOS) {
                int operacionesPorHilo = OPERACIONES_TOTALES / hilos; // Ajustamos operaciones
                ejecutarEscenario(estrategiaLista, hilos, operacionesPorHilo, new double[]{0.33, 0.33, 0.34}, "Escenario 2");
            }

            // Escenario 3: La variación de la cantidad de hilos totales, si se mantiene constante la cantidad de operaciones que ejecuta cada hilo.
            for (int hilos : TOTAL_HILOS) {
                ejecutarEscenario(estrategiaLista, hilos, OPERACIONES_POR_HILO, new double[]{0.33, 0.33, 0.34}, "Escenario 3");
            }
        }
    }

    private static void ejecutarEscenario(estrategia estrategiaLista, int totalThreads, int operacionesPorHilo, double[] proporcion, String nombreEscenario) {
        int addHilos = (int) (totalThreads * proporcion[0]);
        int removeHilos = (int) (totalThreads * proporcion[1]);
        int containsHilos = totalThreads - addHilos - removeHilos;

        long[] tiempos = new long[REPETICIONES];

        for (int i = 0; i < REPETICIONES; i++) {
            Escenario escenario = new Escenario(totalThreads, operacionesPorHilo, estrategiaLista);
            long tiempo = 0;

            tiempo += escenario.ejecutar("add", addHilos);
            tiempo += escenario.ejecutar("remove", removeHilos);
            tiempo += escenario.ejecutar("contains", containsHilos);

            tiempos[i] = tiempo;
        }

        System.out.println("\n Estrategia: " + estrategiaLista.name());
        System.out.println("    Escenario: " + nombreEscenario);
        System.out.println("    Hilos: " + totalThreads);
        System.out.println("    Operaciones por hilo: " + operacionesPorHilo);
        System.out.println("    Proporciones -> add: " + proporcion[0] + ", remove: " + proporcion[1] + ", contains: " + proporcion[2]);
        System.out.println("    Tiempos (ms): " + Arrays.toString(Arrays.stream(tiempos).map(t -> t / 1_000_000).toArray()));
        System.out.println("    Promedio: " + Arrays.stream(tiempos).average().orElse(0) / 1_000_000 + " ms");
        System.out.println("--------------------------------------------------");
    }
}
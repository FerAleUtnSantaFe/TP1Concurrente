package evaluacion;

import estrategia.estrategia;
import nodos.Node;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Escenario {
    private final int totalThreads;
    private final int opsPorHilo;
    private final estrategia estrategiaLista;
    private final Node HEAD;

    public Escenario(int totalThreads, int opsPorHilo, estrategia estrategiaLista) {
        if (totalThreads <= 0 || opsPorHilo <= 0) {
            throw new IllegalArgumentException("El número de hilos y operaciones por hilo debe ser mayor a 0.");
        }

        this.totalThreads = totalThreads;
        this.opsPorHilo = opsPorHilo;
        this.estrategiaLista = estrategiaLista;
        this.HEAD = estrategiaLista.getHEAD();
    }

    public long ejecutar(String tipoOperacion, int cantidadHilos) {
        if (cantidadHilos <= 0) return 0; // Evita ejecutar si no hay hilos
    
        ExecutorService executor = Executors.newFixedThreadPool(cantidadHilos);
        long inicio = System.nanoTime();
    
        for (int i = 0; i < cantidadHilos; i++) {
            executor.execute(new Worker(tipoOperacion, estrategiaLista, HEAD, opsPorHilo));
        }
    
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        long fin = System.nanoTime();
        return fin - inicio; // Tiempo en nanosegundos
    }
    
    public long ejecutar(String tipoOperacion) {
        return ejecutar(tipoOperacion, totalThreads);
    }
}
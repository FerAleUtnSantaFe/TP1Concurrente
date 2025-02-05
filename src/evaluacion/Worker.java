package evaluacion;

import estrategia.estrategia;
import nodos.Node;
import java.util.Random;

public class Worker implements Runnable {
    private final String operacion;
    private final estrategia estrategiaLista;
    private final Node HEAD;
    private final int opsPorHilo;
    private final Random rand = new Random();

    public Worker(String operacion, estrategia estrategiaLista, Node HEAD, int opsPorHilo) {
        this.operacion = operacion;
        this.estrategiaLista = estrategiaLista;
        this.HEAD = HEAD;
        this.opsPorHilo = opsPorHilo;
    }

    @Override
    public void run() {
        for (int i = 0; i < opsPorHilo; i++) {
            int valor = rand.nextInt(10000); // Generamos valores aleatorios

            switch (operacion) {
                case "add":
                    estrategiaLista.addNode(valor, HEAD);
                    break;
                case "remove":
                    estrategiaLista.removeNode(valor, HEAD);
                    break;
                case "contains":
                    estrategiaLista.contains(valor, HEAD);
                    break;
                default:
                    throw new IllegalArgumentException("Operación no soportada: " + operacion);
            }
        }
    }
}
package estrategia;
import Nodos.Node;


public interface estrategia {

    public Node addNode(Object value, Node HEAD);

    public Node removeNode(Object value, Node HEAD);

    public Boolean contains(Object value, Node HEAD);

    public String name();

    public Node getHEAD();

}

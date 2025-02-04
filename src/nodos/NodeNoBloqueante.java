package nodos;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class NodeNoBloqueante extends Node {

    public final AtomicMarkableReference<NodeNoBloqueante> next;

    public NodeNoBloqueante(Object value, Node next) {
        super(value, next);
        NodeNoBloqueante nextNode;
        if(next == null){
            nextNode = null;
        } else {
            nextNode = (NodeNoBloqueante) next;
        }

        this.next = new AtomicMarkableReference<>(nextNode,  false);
    }

    @Override
    public Node getNext(){
        return this.next.getReference();
    }

    @Override
    public void setNext(Node next) throws Exception{
        throw new Exception("Not implemented");
    }

}

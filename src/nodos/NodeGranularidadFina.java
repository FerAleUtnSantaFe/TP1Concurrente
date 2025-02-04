package nodos;

import java.util.concurrent.locks.ReentrantLock;

public class NodeGranularidadFina extends Node {

    private final ReentrantLock lock;

    public NodeGranularidadFina(Object value, Node next){
        super(value, next);
        this.lock = new ReentrantLock();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock(){
        lock.unlock();
    }
}

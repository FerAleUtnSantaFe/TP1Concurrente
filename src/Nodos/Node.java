package Nodos;

public class Node {
    private Object value;
    private Node next;

    public Node(Object value, Node next) {
        this.value = value;
        this.next = next;
    }

    public Object getValue() {
        return value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) throws Exception {
        try {
            this.next = next;
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}

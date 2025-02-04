package nodos;

public class Node {
    private Object value;
    private Node next;
    private int key;

    public Node(Object value, Node next) {
        this.value = value;
        this.next = next;
        this.key = value.hashCode();
    }

    public Object getValue() {
        return value;
    }

    public Node getNext() {
        return next;
    }

    public int getKey() {
        return key;
    }


    public void setNext(Node next) throws Exception {
        try {
            this.next = next;
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}

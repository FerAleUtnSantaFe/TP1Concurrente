package utils;

public class Pair<T> {
    private final T pred;
    private final T curr;
    public final boolean hasCurr;

    public Pair(T pred, T curr) {
        this.pred = pred;
        this.curr = curr;
        this.hasCurr = curr != null;
    }

    public T getPred() {
        return pred;
    }

    public T getCurr() {
    	return curr;
    }   
}
       
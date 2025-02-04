package utils;

public class Pair<T, U> {
    private final T pred;
    private final U curr;

    public Pair(T pred, U curr) {
        this.pred = pred;
        this.curr = curr;
    }

    public T getPred() {
        return pred;
    }

    public U getCurr() {
        return curr;
    }
}

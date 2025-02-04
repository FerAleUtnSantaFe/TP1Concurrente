package estrategia;

import nodos.Node;
import nodos.NodeNoBloqueante;
import utils.Pair;

public class EstrategiaNoBloqueante implements estrategia {

	private Pair<NodeNoBloqueante> find(NodeNoBloqueante HEAD, int key) {
		NodeNoBloqueante pred = null, curr = null, succ = null;
		boolean[] curr_mark = { false };
		boolean curr_removed;
		retry: while (true) // Retry from the beginning...
		{
			pred = HEAD;
			curr = (NodeNoBloqueante) pred.getNext();
			while (curr != null && curr.getKey() < key) {
				succ = curr.next.get(curr_mark);
				while (curr_mark[0]) // If curr was deleted (logically)
				{
					// Try to remove it physically
					curr_removed = pred.next.compareAndSet(curr, succ, false, false);
					if (!curr_removed)
						continue retry; // Pred was also marked, retry...
					curr = succ;
					if (curr == null)
						break;
					succ = curr.next.get(curr_mark);
				}
				pred = curr;
				curr = succ;
			}
			break retry;
		}
		return new Pair<>(pred, curr);
	}

	@Override
	public Node addNode(Object value, Node HEAD) {
		int key = value.hashCode();
		while (true) {
			Pair<NodeNoBloqueante> pair = find(((NodeNoBloqueante) HEAD), key);
			if (!pair.hasCurr || pair.getCurr().getKey() > key) {
				NodeNoBloqueante node = new NodeNoBloqueante(value, pair.getCurr());
				boolean valid = pair.getPred().next.compareAndSet(pair.getCurr(), node, false, false);
				if (valid)
					return node; // If not, retry
			} else
				return pair.getCurr();
		}
	}

	@Override
	public Node removeNode(Object value, Node HEAD) {
		int key = value.hashCode();
		while (true) {
			Pair<NodeNoBloqueante> pair = find((NodeNoBloqueante) HEAD, key);
			if (pair.hasCurr && pair.getCurr().getKey() == key) {
				NodeNoBloqueante succ = (NodeNoBloqueante) pair.getCurr().getNext();
				boolean curr_marked = pair.getCurr().next.attemptMark(succ, true);
				if (curr_marked) {
					pair.getPred().next.compareAndSet(pair.getCurr(), succ, false, false);
					return pair.getCurr();
				}
			} else
				return null;
		}
	}

	@Override
	public Boolean contains(Object value, Node HEAD) {
		boolean marked;
		int key = value.hashCode();
		NodeNoBloqueante curr = (NodeNoBloqueante) HEAD;
		do
			curr = (NodeNoBloqueante) curr.getNext();
		while (curr != null && curr.getKey() < key);
		return curr != null && curr.getKey() == key && !curr.next.isMarked();
	}

	@Override
	public String name() {
		return "Estrategia No Bloqueante";
	}

	@Override
	public Node getHEAD() {
		return new NodeNoBloqueante(Integer.MIN_VALUE, null);
	}

}

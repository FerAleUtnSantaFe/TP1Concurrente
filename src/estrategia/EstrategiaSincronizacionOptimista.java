package estrategia;

import nodos.Node;
import nodos.NodeSincronizacionOptimista;

public class EstrategiaSincronizacionOptimista implements estrategia {

	@Override
	public Node addNode(Object value, Node HEAD) {

		int key = value.hashCode();

		while (true) {
			NodeSincronizacionOptimista pred = (NodeSincronizacionOptimista) HEAD;
			NodeSincronizacionOptimista curr = (NodeSincronizacionOptimista) pred.getNext();

			try {
				while (curr != null && curr.getKey() < key) {
					pred = curr;
					curr = (NodeSincronizacionOptimista) curr.getNext();
				}
				pred.lock();
				if (curr != null)
					curr.lock();

				if (validate(pred, curr, HEAD)) {
					if (curr != null && curr.getKey() == key)
						return curr;
					else {
						NodeSincronizacionOptimista node = new NodeSincronizacionOptimista(value, curr);
						pred.setNext(node);
						return node;
					}
				}
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				pred.unlock();
				if (curr != null)
					curr.unlock();
			}
		}
	}

	@Override
	public Node removeNode(Object value, Node HEAD) {

		int key = value.hashCode();

		while (true) {
			NodeSincronizacionOptimista pred = (NodeSincronizacionOptimista) HEAD;
			NodeSincronizacionOptimista curr = (NodeSincronizacionOptimista) pred.getNext();

			try {
				while (curr != null && curr.getKey() < key) {
					pred = curr;
					curr = (NodeSincronizacionOptimista) curr.getNext();
				}

				pred.lock();
				if (curr != null)
					curr.lock();

				if (validate(pred, curr, HEAD)) {
					if (curr == null || curr.getKey() != key)
						return null;

					pred.setNext(curr.getNext());
					return curr;
				}
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				pred.unlock();
				if (curr != null)
					curr.unlock();
			}
		}
	}

	@Override
	public Boolean contains(Object value, Node HEAD) {
		int key = value.hashCode();

		NodeSincronizacionOptimista pred = null;
		NodeSincronizacionOptimista curr = null;

		try {
			pred = (NodeSincronizacionOptimista) HEAD;
			curr = (NodeSincronizacionOptimista) pred.getNext();

			while (curr != null && curr.getKey() < key) {
				pred = curr;
				curr = (NodeSincronizacionOptimista) curr.getNext();
			}

			pred.lock();
			if (curr != null)
				curr.lock();

			if (validate(pred, curr, HEAD)) {
				return curr != null && curr.getKey() == key;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			if (pred != null)
				pred.unlock();
			if (curr != null)
				curr.unlock();
		}
		return false;
	}

	@Override
	public String name() {
		return "Sincronizacion Optimista";
	}

	@Override
	public Node getHEAD() {
		return new NodeSincronizacionOptimista(Integer.MIN_VALUE, null);
	}

	public Boolean validate(Node predv, Node currv, Node HEAD) {

		NodeSincronizacionOptimista node = (NodeSincronizacionOptimista) HEAD;

		while (node.getKey() <= predv.getKey()) {

			if (node == predv)
				return node.getNext() == currv;

			node = (NodeSincronizacionOptimista) node.getNext();
		}

		return false;
	}

}

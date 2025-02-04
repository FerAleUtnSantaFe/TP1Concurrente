package estrategia;

import nodos.Node;
import nodos.NodeGranularidadFina;
import utils.Pair;

public class EstrategiaGranularidadFina implements estrategia {

	private Pair<NodeGranularidadFina> findPosition(Node HEAD, int key) {
		NodeGranularidadFina pred = (NodeGranularidadFina) HEAD;
		NodeGranularidadFina curr = null;
		pred.lock();
		curr = pred.getNext() != null ? (NodeGranularidadFina) pred.getNext() : null;
		if (curr != null) {
			curr.lock();
			while (curr.getKey() < key) {
				pred.unlock();
				pred = curr;
				curr = curr.getNext() != null ? (NodeGranularidadFina) curr.getNext() : null;
				if (curr != null)
					curr.lock();
				else
					break;
			}
		}
		return new Pair<NodeGranularidadFina>(pred, curr);
	}

	@Override
	public Node addNode(Object value, Node HEAD) {
		int key = value.hashCode();
		Pair<NodeGranularidadFina> nodes = null;
		try {
			nodes = findPosition(HEAD, key);
			if (!nodes.hasCurr || nodes.getCurr().getKey() > key) {
				NodeGranularidadFina node = new NodeGranularidadFina(value, nodes.getCurr());
				nodes.getPred().setNext(node);
				return node;
			} else
				return nodes.getCurr();
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			assert nodes != null;
			nodes.getPred().unlock();
			if (nodes.hasCurr)
				nodes.getCurr().unlock();
		}
		return null;
	}

	@Override
	public Node removeNode(Object value, Node HEAD) {
		int key = value.hashCode();
		Pair<NodeGranularidadFina> nodes = null;
		try {
			nodes = findPosition(HEAD, key);
			if (nodes.hasCurr && nodes.getCurr().getKey() == key) {
				nodes.getPred().setNext(nodes.getCurr().getNext());
				return nodes.getCurr();
			} else
				return null;
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			assert nodes != null;
			nodes.getPred().unlock();
			if (nodes.hasCurr)
				nodes.getCurr().unlock();
		}
		return null;
	}

	@Override
	public Boolean contains(Object value, Node HEAD) {
		int key = value.hashCode();
		Pair<NodeGranularidadFina> nodes = null;
		try {
			nodes = findPosition(HEAD, key);
			return (nodes.hasCurr && nodes.getCurr().getKey() == key);
		} finally {
			assert nodes != null;
			nodes.getPred().unlock();
			if (nodes.hasCurr)
				nodes.getCurr().unlock();
		}
	}

	@Override
	public String name() {
		return "Fine-Grained Strategy";
	}

	@Override
	public Node getHEAD() {
		return new NodeGranularidadFina(Integer.MIN_VALUE, null);
	}

}

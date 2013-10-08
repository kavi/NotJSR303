package notjsr;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Path;

import notjsr.NotJSRPath.PathNode;

public class ObjectGraph {

	private Node activeNode;
	private Node root;

	public ObjectGraph(Class<?> type) {
		super();
		activeNode = new Node(null, type, null);
		root = activeNode;
	}

	public void addActiveChild(Class<?> type, String name) {
		Node newChild = new Node(name, type, activeNode);
		activeNode.addChild(newChild);
		activeNode = newChild;
	}

	public void addActiveChild(Class<?> type, String name, int index) {
		Node newChild = new Node(name, index, type, activeNode);
		activeNode.addChild(newChild);
		activeNode = newChild;
	}

	public void addActiveChild(Class<?> type, String name, Object key) {
		Node newChild = new Node(name, key, type, activeNode);
		activeNode.addChild(newChild);
		activeNode = newChild;
	}

	public void makeParentActive() {
		if (activeNode.getParent() == null) {
			return;
			// throw new RuntimeException("Cannot goto roots parent");
		}
		activeNode = activeNode.getParent();
	}

	public String graphToString() {
		StringBuilder stringbuilder = new StringBuilder();
		printNode(root, "", stringbuilder);
		return stringbuilder.toString();
	}

	private StringBuilder printNode(Node node, String indent,
			StringBuilder string) {
		String index = node.getIndex() != null ? "[" + node.getIndex() + "]"
				: "";
		index = node.getKey() != null ? "[" + node.getKey() + "]" : "";
		String name = node.getName() != null ? node.getName() : "";
		string.append(indent + name + index + ":"
				+ node.getType().getSimpleName() + "\n");
		// System.out.println(indent + node+ ":" + node.getParent());
		for (Node n : node.getChildren()) {
			printNode(n, indent + "--", string);
		}
		return string;
	}

	public NotJSRPath getActiveNodePath() {
		List<Path.Node> path = new LinkedList<Path.Node>();
		Node current = activeNode;
		while (current != null) {
			path.add(0, new PathNode(current.getType(), current.getName(), current.getIndex(), current.getKey()));
			current = current.getParent();
		}
		return new NotJSRPath(path);
	}

	public static class Node {
		private Object key;
		private String name;
		private Integer index;
		private Class<?> type;
		private Node parent;
		private List<Node> children = new LinkedList<ObjectGraph.Node>();

		public Node(String name, Class<?> type, Node parent) {
			super();
			this.name = name;
			this.type = type;
			this.parent = parent;
		}

		public Node(String name, Integer index, Class<?> type, Node parent) {
			super();
			this.name = name;
			this.index = index;
			this.type = type;
			this.parent = parent;
		}

		public Node(String name, Object key, Class<?> type, Node parent) {
			super();
			this.name = name;
			this.key = key;
			this.type = type;
			this.parent = parent;
		}

		public Node getParent() {
			return parent;
		}

		public List<Node> getChildren() {
			return Collections.unmodifiableList(children);
		}

		public void addChild(Node n) {
			children.add(n);
		}

		public String getName() {
			return name;
		}

		public Class<?> getType() {
			return type;
		}

		public Integer getIndex() {
			return index;
		}

		public Object getKey() {
			return key;
		}
	}
}

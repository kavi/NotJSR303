package notjsr;

import java.util.Iterator;
import java.util.List;

import javax.validation.ElementKind;
import javax.validation.Path;

public class NotJSRPath implements Path {

	@Override
	public Iterator<Node> iterator() {
		return path.iterator();
	}

	private List<Node> path;

	public NotJSRPath(List<Node> path) {
		super();
		this.path = path;
	}

	public void addElement(PathNode element) {
		path.add(element);
	}

	public String pathAsString() {
		String s = "";
		for (Node n : path) {
			PathNode pe = (PathNode) n;
			if (pe.getName() == null) {
				s += pe.getType().getSimpleName();
			} else {
				String index = pe.getIndex() != null ? "[" + pe.getIndex() + "]" : "";
				index = pe.getKey() != null ? "[" + pe.getKey() + "]" : "";
//				String type = "(" + pe.getType().getSimpleName() + ")";
				s += "."  + pe.getName() + index;
			}
		}
		return s;
	}

	public static class PathNode implements Node {
		private Class<?> type;
		private String name;
		private Integer index;
		private Object key;

		public PathNode(Class<?> type, String name) {
			super();
			this.type = type;
			this.name = name;
		}

		public PathNode(Class<?> type, String name, Object key) {
			super();
			this.type = type;
			this.name = name;
			this.key = key;
		}

		public PathNode(Class<?> type, String name, int index) {
			super();
			this.type = type;
			this.name = name;
			this.index = index;
		}
		
		public PathNode(Class<?> type, String name, Integer index, Object key) {
			super();
			this.type = type;
			this.name = name;
			this.index = index;
			this.key = key;
		}

		@Override
		public <T extends Node> T as(Class<T> arg0) {
			throw new UnsupportedOperationException("as not supported in NotJSR303");
		}

		@Override
		public Object getKey() {
			return key;
		}

		@Override
		public ElementKind getKind() {
			if (name != null) {
				return ElementKind.PROPERTY;
			} else {
				return ElementKind.BEAN;
			}
		}

		@Override
		public boolean isInIterable() {
			return index != null;
		}
		
		public Class<?> getType() {
			return type;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Integer getIndex() {
			return index;
		}
	}
}

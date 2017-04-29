import java.util.Map;

public class HuffmanTree {
	
	private static class Node {
		public int value;
		public Node left;
		public Node right;
		
		public Node(int value, Node left, Node right) {
			this.value = value;
			this.left = left;
			this.right = right;
		}
	}
	
	private Node root;
	
	
	public HuffmanTree(Map<Short, Integer> m) {
		
	}

	public HuffmanTree(BitInputStream in) {
		
	}
	
	public void serialize(BitOutputStream out) {
		
	}
	
	public void encode(BitInputStream in, BitOutputStream out) {
		
	}
	
	public void decode(BitInputStream in, BitOutputStream out) {
		
	}
}

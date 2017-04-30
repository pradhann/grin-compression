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
		root = HuffmanTreeHelper(in);
	}

	public Node HuffmanTreeHelper(BitInputStream in) {
		Node cur = null;
		if (in.hasBits()) {
			int bit = in.readBit();
			if(bit == 0) {
				cur = new Node(in.readBits(9), null, null);
			} else {
				cur = new Node(-1, HuffmanTreeHelper(in), HuffmanTreeHelper(in));
			}
		}
		return cur;
	}

	public void serialize(BitOutputStream out) {
		Node cur = this.root;
		if(cur.value != -1) {
			out.writeBit(0);
			out.writeBits(cur.value, 9);
		} else {
			out.writeBit(1);
			serialize
		}
		}


	public void encode(BitInputStream in, BitOutputStream out) {

	}

	public void decode(BitInputStream in, BitOutputStream out) {
		while(in.hasBits()) {
			Node cur = this.root;
			while(cur.value == -1) {
				if(in.readBit() == 0) {
					cur = cur.left;
				} else {
					cur = cur.right;
				}
			}
			out.writeBits(cur.value, 9);
		}
	}
}

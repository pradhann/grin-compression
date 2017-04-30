import java.util.*;


public class HuffmanTree {

	private static class Node implements Comparator<Node> {

		private char type;
		private  int freq;
		private Node left;
		private Node right;

		public Node(char type, int freq, Node left, Node right) {
			this.type = type;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		public int getFreq() {
			return freq;
		}

		public int compare(Node o1, Node o2) {
			if(o1.getFreq() < o2.getFreq()) {
				return -1; 
			} else if (o2.getFreq() < o1.getFreq()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private Node root;


	public HuffmanTree(Map<Short, Integer> m) {

		m.put((short) 256,1);
		PriorityQueue<Node> buffer = new PriorityQueue<>();
		ArrayList<Short> key = new ArrayList<Short>(m.keySet());

		for(short sh : key) {
			Node allLeaf = new Node((char) sh, m.get(sh), null, null);
			buffer.add(allLeaf);
		}

		while(buffer.size() > 1) {
			Node first = buffer.poll();
			Node second = buffer.poll();
			int interFreq = first.getFreq() + second.getFreq();
			Node cur = new Node((Character) null, interFreq, first, second);
			buffer.add(cur);
		}

		root = buffer.poll();
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

	}

	public void encode(BitInputStream in, BitOutputStream out) {

	}

	public void decode(BitInputStream in, BitOutputStream out) {

	}


}
}

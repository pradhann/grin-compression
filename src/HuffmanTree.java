import java.util.*;


public class HuffmanTree {


	private static class Node implements Comparator<Node> {

		private short type;
		private  int freq;
		private Node left;
		private Node right;

		public Node(short type, int freq, Node left, Node right) {
			this.type = type;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
		

		public int getFreq() {
			return freq;
		}
		
		public short getBits() {
			return type;
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
		
		public boolean isLeaf() {
			return type != -1;
		}
	}

	private Node root;


	public HuffmanTree(Map<Short, Integer> m) {

		m.put((short) 256,1);
		PriorityQueue<Node> buffer = new PriorityQueue<>();
		ArrayList<Short> key = new ArrayList<Short>(m.keySet());

		for(short sh : key) {
			Node allLeaf = new Node(sh, m.get(sh), null, null);
			buffer.add(allLeaf);
		}

		while(buffer.size() > 1) {
			Node first = buffer.poll();
			Node second = buffer.poll();
			int interFreq = first.getFreq() + second.getFreq();
			Node cur = new Node((short) -1, interFreq, first, second);
			buffer.add(cur);
		}

		root = buffer.poll();

	}



	public HuffmanTree(BitInputStream in) {
		root = HuffmanTreeHelper(in);
	}

	public Node HuffmanTreeHelper(BitInputStream in) {
		int temp = in.readBit();
		if(temp == 1) {
			return new Node(HuffmanTreeHelper(in), HuffmanTreeHelper(in));
		} else if (temp == 0) {
			return new Node (short) in.readBits(9);
		} else
			throw new IllegalArgumentException();
	}



	public void serialize(BitOutputStream out) {
		serializeHelper(root, out);
	}

	public static void serializeHelper(Node cur, BitOutputStream out) {
		if(cur != null) {
			if(cur.type != '\u0000') {
				out.writeBit(0);
				out.writeBits(cur.type, 9);
			} else {
				out.writeBit(1);
				serializeHelper(cur.left, out);
				serializeHelper(cur.right, out);
			}
		}
	}






	public void encode(BitInputStream in, BitOutputStream out) {

	}

	public void decode(BitInputStream in, BitOutputStream out) {
		int temp = in.readBit();
		Node cur = root;
		while(temp != -1) {
			while(!cur.isLeaf()) {
				if(temp == 1) {
					cur = cur.left;
				} else {
					cur = cur.right;
				}
				temp = in.readBit();
			}
			out.writeBits(cur.type, 9);
		}
			cur = root;
	}
}


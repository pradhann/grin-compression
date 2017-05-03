import java.util.*;


public class HuffmanTree {
	private Node root;

	private static class Node implements Comparable<Node> {

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

		public Node(short type, int freq) {
			this.type = type;
			this.freq = freq;
			this.left = null;
			this.right = null;
		}


		public int getFreq() {
			return freq;
		}


		public int compareTo(Node o1) {
			if(o1.getFreq() > this.getFreq()) {
				return -1; 
			} else if (this.getFreq() > o1.getFreq()) {
				return 1;
			} else {
				return 0;
			}
		}

		public boolean isLeaf() {
			return type != 0;
		}
	}




	public HuffmanTree(Map<Short, Integer> m) {

		m.put((short) 256,1);
		PriorityQueue<Node> buffer = new PriorityQueue<>();
		ArrayList<Short> key = new ArrayList<Short>(m.keySet());

		for(short sh : key) {
			Node allLeaf = new Node(sh, m.get(sh));
			buffer.add(allLeaf);
		}

		while(buffer.size() > 1) {
			Node first = buffer.poll();
			Node second = buffer.poll();
			int interFreq = first.getFreq() + second.getFreq();
			Node cur = new Node((short) 0, interFreq, first, second);
			buffer.add(cur);
		}

		root = buffer.poll();

	}



	public HuffmanTree(BitInputStream in) {
		root = HuffmanTreeHelper(in);
	}

	public Node HuffmanTreeHelper(BitInputStream in) {
		int temp = in.readBit();
		if(temp == 0) {
			return new Node ((short) in.readBits(9), 0);
		} else if (temp == 1) {
			return new Node((short) 0,0, HuffmanTreeHelper(in), HuffmanTreeHelper(in));
		} else
			throw new IllegalArgumentException();		

	}



	public void serialize(BitOutputStream out) {
		serializeHelper(root, out);
	}

	public static void serializeHelper(Node cur, BitOutputStream out) {
		if(cur != null) {
			if(cur.type != 0) {
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
		Map<Short, String> allPaths = new HashMap<>();
		findAllPaths(root, "", allPaths);
		while(in.hasBits()) {
			short cur = (short) in.readBits(8);
			if(allPaths.containsKey(cur)) {
				String curPath = allPaths.get(cur);
				for(char c : curPath.toCharArray() ) {
					out.writeBit(c - 48);
				}
			}
		}
		String eof = allPaths.get((short) 256);
		for(char c : eof.toCharArray() ) {
			out.writeBit(c - 48);
		}
		
	}



	private void findAllPaths(Node cur, String s, Map<Short, String> allPaths) {
		if(cur == null) {
			return;
		} else if (cur.left != null && cur.right != null) {
			findAllPaths(cur.left, s + "0", allPaths);
			findAllPaths(cur.right, s + "1", allPaths);
		} else {
			allPaths.put(cur.type, s);
		}
	}


	private boolean decodeHelper (BitInputStream in, BitOutputStream out, Node cur) {

		if (cur.right == null && cur.left == null) {
			if (cur.type != 256) {
				out.writeBits(cur.type, 8);
				return true;
			} else {
				return false;
			}
		}
		int bit = in.readBit(); 
		if (bit == 0) {
			decodeHelper (in, out, cur.left);
		} else if (bit == 1) {
			decodeHelper (in, out, cur.right);
		}
		return true;
	}

	public void decode(BitInputStream in, BitOutputStream out){
		boolean check = true;
		while (in.hasBits() && check) {
			check = decodeHelper (in, out, root);
		}
	}
}


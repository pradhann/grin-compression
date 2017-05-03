
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


		/**
		 *  Compares two nodes based upon the frequency field.
		 *  
		 *  @return int a comparison integer
		 */
		public int compareTo(Node o1) {
			if(o1.getFreq() > this.getFreq()) {
				return -1; 
			} else if (this.getFreq() > o1.getFreq()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Creates a Huffman tree from the frequency map of characters
	 *  for encoding a file.
	 * 
	 * @param m the frequency map for each character in the file
	 */
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


	/**
	 *  Creates a Huffman tree from the serialized version
	 *  of the tree in the compressed file.
	 * 
	 * @param in a BitInputStream for the compressed file
	 */
	public HuffmanTree(BitInputStream in) {
		root = HuffmanTreeHelper(in);
	}

	/**
	 * Recursively builds the Huffman tree from the
	 *  compressed file. 
	 * 
	 * @param in a BitInputStream of the compressed file
	 * @return Node a node of the tree
	 */
	public Node HuffmanTreeHelper(BitInputStream in) {
		int temp = in.readBit();
		if(temp == 0) {
			return new Node ((short) in.readBits(9), 0);
		} else if (temp == 1) {
			return new Node((short) 0,0, HuffmanTreeHelper(in), HuffmanTreeHelper(in));
		} else
			throw new IllegalArgumentException();		

	}


	/**
	 *  Serializes the Huffman tree in preorder.
	 * 
	 * @param out a BitOutputStream of the output file
	 */
	public void serialize(BitOutputStream out) {
		serializeHelper(root, out);
	}

	/**
	 * Recursively writes each node of the Huffman tree to the given
	 * output stream.
	 * 
	 * @param cur a Node of the Huffman tree
	 * @param out a BitOutputStream to write to
	 */
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

	/**
	 * Encodes the payload of the file to compress using values
	 * obtained from the Huffman tree and writes the encodings 
	 * to the given output stream.
	 * 
	 * @param in a BitInputStream of the file to compress
	 * @param out a BitOutputStream of the file to write to
	 */
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


	/**
	 *  Maps each character in the Huffman tree to its encoded value.
	 *  
	 * @param cur a Node of the Huffman tree
	 * @param s a string of the encoded value
	 * @param allPaths a map of short and string pairs
	 */
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


	/**
	 * Recursively reads the payload of the compressed file and locates the correct
	 * character to decode using the encoded value as the path in the tree.
	 * 
	 * @param in a BitInputStream of the compressed file
	 * @param out a BitOutputStream to write the decoded characters to
	 * @param cur a Node of the Huffman tree
	 * @return boolean if the character encountered is the eof character
	 */
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
			return decodeHelper (in, out, cur.left);
		} else {
			return decodeHelper (in, out, cur.right);
		}
	}

	/**
	 * Decodes the compressed file using the Huffman tree until 
	 * the eof character is encountered.
	 * 
	 * @param in a BitInputStream of the compressed file
	 * @param out a BitOutputStream to write the decoded characters to
	 */
	public void decode(BitInputStream in, BitOutputStream out){
		boolean check = true;
		while (in.hasBits() && check) {
			check = decodeHelper (in, out, root);
		}
	}
}


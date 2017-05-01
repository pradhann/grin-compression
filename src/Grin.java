import java.io.IOException;

public class Grin {

	public static void decode(String infile, String outfile) throws IOException {
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		
		
		if(in.readBits(32) != 1846) {
			throw new IllegalArgumentException();
		}
		
		HuffmanTree hTree = new HuffmanTree(in);
		hTree.decode(in, out);
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

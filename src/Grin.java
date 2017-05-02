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
		
		in.close();
		out.close();
	}
	
	public static void main(String[] args) throws IOException {
		if (args[0].equals("decode")) {
		String infile = args[1];
		String outfile = args[2];
		decode(infile, outfile);
		}

	}

}

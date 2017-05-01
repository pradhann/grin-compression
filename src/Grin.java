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

	public static void main(String[] args)  throws IOException {
		if(args.length != 3) {
			throw new IllegalArgumentException();
		}

		String command = args[0];
		String infile = args[1];
		String outfile = args[2];

		switch (command) {
		case "decode" :
			Grin.decode(infile, outfile);
			break;

		case "encode" :
			//insert
			break;

		default:
			throw new IllegalArgumentException();

		}
	}
}

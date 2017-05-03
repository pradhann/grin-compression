import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
	public static void encode(String infile, String outfile) throws IOException {
		Map<Short, Integer> map = createFrequencyMap(infile);
		
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		
		HuffmanTree hTree = new HuffmanTree(map);
		
		out.writeBits(1846, 32);
		
		hTree.serialize(out);
		hTree.encode(in, out);
		
		in.close();
		out.close();
	}
	
	
	public static Map<Short, Integer>  createFrequencyMap(String file) throws IOException {
		Map<Short, Integer> map = new HashMap<>();
		BitInputStream infile = new BitInputStream(file);
		
		while(infile.hasBits()) {
			short current = (short) infile.readBits(8);
			if(!map.containsKey(current)) {
				map.put(current, 1);
			} else {
				int temp = map.get(current);
				map.put(current, ++temp);
			}
		}
		return map;
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
			Grin.encode(infile, outfile);
			break;

		default:
			throw new IllegalArgumentException();

		}
	}
}

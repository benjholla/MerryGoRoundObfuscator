package huffman;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Huffman {
	
	public static Map<Block,Integer> getBlockFrequencies(File file, int blockSize) throws Exception {
		// error checking
		if(Math.IEEEremainder(8.0, (double)blockSize) != 0){
			throw new IllegalArgumentException("Invalid block size");
		}
		
		Map<Block,Integer> frequencies = new HashMap<Block,Integer>();
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		try {
			// read until end of file
			while(true){
				Byte b = raf.readByte();
				// if block size is less than 8 we will have to parse multiple
				// blocks out of the byte read from the file
				for(int i=0; i<8; i+=blockSize){
					addBlock(blockSize, frequencies, (byte)(b>>i));
				}
			}
		} catch (EOFException e){
			// reached the end of the file
		} finally {
			raf.close();
		}

		return frequencies;
	}
	
	private static void addBlock(int blockSize, Map<Block, Integer> frequencies, Byte b) {
		Block block = new Block(blockSize, b);
		if(frequencies.containsKey(block)){
			frequencies.put(block, frequencies.get(block) + 1);
		} else {
			frequencies.put(block, 1);
		}
	}
	
	public static HuffmanTree getHuffmanTree(Map<Block, Integer> frequencies, int blockSize){
		return new HuffmanTree(frequencies, blockSize);
	}
	
	// returns a mapping of Blocks to binary Strings
	public static Map<Block,String> getEncodingKey(HuffmanTree tree){
		Map<Block,String> encodingKey = new HashMap<Block,String>();
		dfs(tree.root, encodingKey, "");
		return encodingKey;
	}
	
	// iterates the tree with a depth first search and assigns a binary encoding string to each leaf node
	private static void dfs(HuffmanNode node, Map<Block,String> encodingKey, String currentString){
		if(node.isLeaf()){
			encodingKey.put(node.getBlock(), currentString);
		} else {
			if(node.left != null){
				dfs(node.left, encodingKey, new String(currentString + "0"));
			}
			if(node.right != null){
				dfs(node.right, encodingKey, new String(currentString + "1"));
			}
		}
	}
	
	public static int compress(File input, File output, HuffmanTree tree) throws IOException {
		System.out.println("Compressing: " + input.getAbsolutePath() + " with block size " + tree.blockSize);
				
		Map<Block,String> encodingKey = getEncodingKey(tree);
		RandomAccessFile raf = new RandomAccessFile(input, "r");
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(output));
		String out = "";
		Byte in;
		long fileLength = raf.length();
		// read until end of file
		while(raf.getFilePointer() < fileLength){
			// read in a byte
			in = raf.readByte();
			// for each block size in the read in byte (1, 2, 4, and 8)
			// create a Block and look up its corresponding binary string encoding
			for(int i=8-tree.blockSize; i>=0; i-=tree.blockSize){
				Block b = new Block(tree.blockSize, (byte)(in >> i));
				
				// debug
				//System.out.println("Examining: " + Block.byteToBinaryString(in) + " ("+ Block.byteToHexString(in) + "), index: " + i + ", block: " + b.toString());
				
				out += getBlockEncoding(b, encodingKey);
			}
			if(out.length() >= 8){
				out = writeBytes(out, os);
			}
		}
		raf.close();
		
		int excess = 0;
		if(out.length() > 0){
			excess = 8-out.length();
			// pad with zeros if on uneven byte boundary
			for(int i=0; i<excess; i++){
				out += "0";
			}
			out = writeBytes(out, os);
		}
		os.close();

		System.out.println("Output: " + output.getAbsolutePath() + "\n");
		
		return excess;
	}
	
	public static void decompress(File input, File output, HuffmanTree tree, int excess) throws IOException {
		System.out.println("Decompressing: " + input.getAbsolutePath() + " with block size " + tree.blockSize);
		
		RandomAccessFile raf = new RandomAccessFile(input, "r");
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(output));
		String out = "";
		Byte in;
		HuffmanNode current = tree.root;
		long fileLength = raf.length();
		// read until end of file
		while(raf.getFilePointer() < fileLength){
			// read in a byte
			in = raf.readByte();
			// walk the tree until you hit a leaf, then write out the leaf Block value
			// last byte may have excess to be discarded from the 8 bit boundary
			int currentExcess = 0;
			if(raf.getFilePointer() == fileLength){
				currentExcess = excess;
			}
			for(int i=7; i>=currentExcess; i--){
				// debug
				//System.out.println("Examining: " + Block.byteToBinaryString(in) + " ("+ Block.byteToHexString(in) + "), index: " + i + ", index mod2: " + (int)Math.abs((in >> i) % 2));
				
				if((in >> i) % 2 == 0){
					current = current.left;
				} else {
					current = current.right;
				}
				if(current.isLeaf()){
					out += current.getBlock().getStringValue();
					if(out.length() >= 8){
						out = writeBytes(out, os);
					}
					current = tree.root;
				}
			}
		}
		raf.close();
		os.close();

		System.out.println("Output: " + output.getAbsolutePath() + "\n");
	}
	
	// take a string of binary 1's and 0's and writes byte blocks out (does not write remainders)
	private static String writeBytes(String out, BufferedOutputStream os) throws IOException {
		byte b = 0x00;
		while(out.length() >= 8){
			int i = 0;
			for(int j=7; j>=0; j--){
				if(out.charAt(i++) == '1'){
					b = Block.setBit(b, j);
				}
			}
			
			// debug
			//System.out.println("Writing: " + Block.byteToBinaryString(b) + " ("+ Block.byteToHexString(b) + ")");
			
			os.write(b);
			out = out.substring(8);
		}
		return out;
	}
	
	// returns a binary string for a given Block
	private static String getBlockEncoding(Block b, Map<Block,String> encodingKey){
		for(Entry<Block,String> entry : encodingKey.entrySet()){
			if(entry.getKey().equals(b.blockSize, b.value)){
				return entry.getValue();
			}
		}
		return null;
	}
}

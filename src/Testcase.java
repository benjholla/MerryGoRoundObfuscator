import huffman.Block;
import huffman.Huffman;
import huffman.HuffmanTree;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;


public class Testcase {

	public static void main(String[] args) throws Exception {
		File file = new File("/Users/benjholla/Desktop/huffman.txt");
		test(file, 1);
		test(file, 2);
		test(file, 4);
		test(file, 8);
	}

	private static void test(File file, int blockSize) throws Exception {
		Map<Block, Integer> frequencies = Huffman.getBlockFrequencies(file, blockSize);
		System.out.println("Block frequencies:\n" + frequencies + "\n");
		
		HuffmanTree tree = Huffman.getHuffmanTree(frequencies, blockSize);
		Map<Block,String> encodingKey = Huffman.getEncodingKey(tree);
		System.out.println("Encoding key:\n" + encodingKey + "\n");
		
		int excess = Huffman.compress(file, new File(file.getAbsolutePath() + ".huff"), tree);
		Huffman.decompress(new File(file.getAbsolutePath() + ".huff"), new File(file.getAbsolutePath() + ".huff.txt"), tree, excess);
	
		if(compareFiles(file, new File(file.getAbsolutePath() + ".huff.txt"))){
			System.out.println("SUCCESS");
		} else {
			System.out.println("FAILURE: Input does not equal output for block size " + blockSize + ".");
		}
		
		System.out.println("\n----------------------------------------------------------------------------------\n");
	}

	public static boolean compareFiles(File a, File b){
		try{
			RandomAccessFile rafA = new RandomAccessFile(a, "r");
			RandomAccessFile rafB = new RandomAccessFile(b, "r");
			
			// check same file length
			if(rafA.length() != rafB.length()){
				return false;
			}
			
			// compare byte for byte
			if(rafA.length() > 0){
				while(rafA.getFilePointer() < rafA.length()){
					if(rafA.readByte() != rafB.readByte()){
						return false;
					}
				}
			}
			
			// equal or both zero length files
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
}

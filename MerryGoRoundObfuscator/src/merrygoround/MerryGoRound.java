package merrygoround;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

import merrygoround.generators.CodeGenerator;
import merrygoround.huffman.Block;
import merrygoround.huffman.Huffman;
import merrygoround.huffman.HuffmanTree;


public class MerryGoRound {

	// create a huffman like recursive structure with n methods
	public static void main(String[] args) throws Exception {
		
		// block size has to divide evenly into one byte
		System.out.println("Valid Block Sizes\n-----------------------");
		for(int i=1; i<=8; i*=2){
			System.out.println(i + " bit: n <= " + (int)Math.pow(2, i) + " leaf methods");
		}

		// get program parameters
		Scanner scanner = new Scanner(System.in);
		
		boolean validBlockSize = true;
		int blockSize;
		do {
			System.out.print("\nSelect a block size: ");
			blockSize = scanner.nextInt();
			if(Math.IEEEremainder(8.0, (double)blockSize) == 0){
				validBlockSize = true;
			} else {
				System.out.print("\nError: Invalid block size.");
				validBlockSize = false;
			}
		} while(!validBlockSize);
			
		
		boolean validFilePath = true;
		File input = null;
		do {
			System.out.print("\nEnter path of file to encode: ");
			scanner.nextLine(); // trash
			try {
				String line = scanner.nextLine();
				input = new File(line);
				if(!input.exists()){
					throw new Exception("File does not exist.");
				}
			} catch (Exception e){
				System.out.println("\nError: File path not valid.");
				scanner.nextLine(); // trash
				validFilePath = false;
			}
		} while(!validFilePath);

		// print some encoder attributes
		System.out.println("\n----------------Encoder Properties----------------\n");
		
		Map<Block, Integer> frequencies = Huffman.getBlockFrequencies(input, blockSize);
		System.out.println("Block frequencies:\n" + frequencies + "\n");
		
		HuffmanTree tree = Huffman.getHuffmanTree(frequencies, blockSize);
		
		Map<Block,String> encodingKey = Huffman.getEncodingKey(tree);
		System.out.println("Encoding key:\n" + encodingKey + "\n");
		
		File output = new File(input.getAbsolutePath() + ".huff");
		
		int excess = Huffman.compress(input, output, tree);
		
		System.out.println("Payload Size: " + output.length() + " bytes");
		System.out.println("Excess: " + excess + " bits");
		
		// print some encoder attributes
		System.out.println("\n----------------Obfuscation Options----------------\n");
		
		boolean validRecursionOption = true;
		boolean useRecursion = false;
		do {
			System.out.println("Option: Recursively reconstruct payload.  Alternatively a while loop is used.");
			System.out.println("Note: This may not be a workable option for large files as the stack may overflow.");
			System.out.print("Use recursion? (y/n): ");
			String line = scanner.nextLine();
			if(line.toUpperCase().equals("Y") || line.toUpperCase().equals("YES")){
				validRecursionOption = true;
				useRecursion = true;
			} else if(line.toUpperCase().equals("N") || line.toUpperCase().equals("NO")){
				validRecursionOption = true;
				useRecursion = false;
			} else {
				System.out.println("\nError: Invalid option, enter \"y\" or \"n\".");
				validRecursionOption = false;
			}
		} while(!validRecursionOption);

		System.out.println("\n----------------Begin Source----------------\n");
		
		// create call graph decoder
		CodeGenerator.readFromFileFramework(tree, blockSize, input.length(), useRecursion);
		CodeGenerator.constructCallGraph(tree.root, useRecursion);
		
		System.out.println("\n----------------End Source----------------\n");
	}

}

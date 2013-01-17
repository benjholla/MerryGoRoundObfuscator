import huffman.Block;
import huffman.Huffman;
import huffman.HuffmanNode;
import huffman.HuffmanTree;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Map;


public class MerryGoRound {

	// create a huffman like recursive structure with n methods
	public static void main(String[] args) throws Exception {
		
		/*
		// block size has to divide evenly into one byte
		System.out.println("Block Size\n-----------------------");
		for(int i=1; i<=8; i*=2){
			System.out.println(i + " bit: n <= " + (int)Math.pow(2, i) + " methods");
		}
		System.out.println("\nSelect a block size: ");
		
		/*
		if(Math.IEEEremainder(8.0, (double)selectedBlockSize) == 0){
			
		} else {
			// invalid block size
		}
		*/
		
		File file = new File("/Users/benjholla/Desktop/huffman.txt");
		int blockSize = 2;
		
		Map<Block, Integer> frequencies = Huffman.getBlockFrequencies(file, blockSize);
		System.out.println("Block frequencies:\n" + frequencies + "\n");
		
		HuffmanTree tree = Huffman.getHuffmanTree(frequencies, blockSize);
		
		Map<Block,String> encodingKey = Huffman.getEncodingKey(tree);
		System.out.println("Encoding key:\n" + encodingKey + "\n");
		
		int excess = Huffman.compress(file, new File(file.getAbsolutePath() + ".huff"), tree);
		System.out.println("Excess: " + excess + "\n\n");
		
		boolean useRecursion = false;
		
		// create call graph decoder
		readFromFileFramework(tree, blockSize, useRecursion);
		constructCallGraph(tree.root, useRecursion);
	}
	
	// performs a DFS on the huffman tree, while creating the call graph of the static decompressor
	public static void constructCallGraph(HuffmanNode node, boolean useRecursion){
		startMethod(nodeToMethodName(node));
		if(node.isLeaf()){
			writeByte(node.getBlock().value);
			if(useRecursion){
				callMethod(nodeToMethodName(getRoot(node)));
			}
		} else {
			writeCondition(node.left, node.right);
		}
		endMethod();
		
		
		if(node.left != null){
			constructCallGraph(node.left, useRecursion);
		}
		if(node.right != null){
			constructCallGraph(node.right, useRecursion);
		}
	}
	
	public static void readFromFileFramework(HuffmanTree tree, int blockSize, boolean useRecursion){
		System.out.println("private static RandomAccessFile si = null;");
		System.out.println("private static BufferedOutputStream so = null;");
		System.out.println("private static int ii = 0;");
		System.out.println("private static int oi = 0;");
		System.out.println("private static int bs = " + blockSize + ";");
		System.out.println("private static byte[] bi = new byte[1];");
		System.out.println("private static byte[] bo = new byte[1];");
		System.out.println("");
		
		System.out.println("// args[0] = input stream");
		System.out.println("// args[1] = output stream");
		System.out.println("public static void main(String args[]) throws IOException {");
		System.out.println("si = new RandomAccessFile(new File(args[0]), \"r\");");
		System.out.println("so = new BufferedOutputStream(new FileOutputStream(new File(args[1])));");
		System.out.println("try {");
		if(!useRecursion){
			System.out.println("while(true){");
		}
		callMethod(nodeToMethodName(tree.root));
		if(!useRecursion){
			System.out.println("}");
		}
		System.out.println("}");
		System.out.println("catch (Exception e){");
		System.out.println("// invalid checksum");
		System.out.println("}");
		System.out.println("finally {");
		System.out.println("si.close();");
		System.out.println("so.close();");
		System.out.println("}");
		System.out.println("}");
		System.out.println("");

		System.out.println("private static byte read() throws IOException {");
		System.out.println("if (ii == 0) {");
		System.out.println("bi[0] = si.readByte();");
		System.out.println("ii = 8;");
		System.out.println("}");
		System.out.println("byte result = (byte)((bi[0] & 0xFF) >> (ii-1));");
		System.out.println("ii -= bs;");
		System.out.println("return result;");
		System.out.println("}");
		System.out.println("");
		
		System.out.println("private static void write(byte b) throws IOException {");
		System.out.println("bo[0] = (byte)(bo[0] | (b << (7-oi)));");
		System.out.println("oi += bs;");
		System.out.println("if(oi == 8){");
		System.out.println("so.write(bo[0]);");
		System.out.println("bo[0] = 0x00;");
		System.out.println("oi = 0;");
		System.out.println("}");
		System.out.println("}");
		System.out.println("");	
	}
	
	public static void startMethod(String name){
		System.out.println("private static void " + name + "() throws IOException {");
	}
	
	public static void callMethod(String name){
		System.out.println(name + "();");
	}
	
	public static void writeByte(byte b){
		System.out.println("byte b = (byte)0x" + String.format("%02X", b)  + ";");
		System.out.println("write(b);");
	}
	
	public static void writeCondition(HuffmanNode left, HuffmanNode right)
	{
		System.out.println("if(read() % 2 == 0){");
		callMethod(nodeToMethodName(left));
		System.out.println("}");
		
		if(right != null){
			System.out.println("else {");
			callMethod(nodeToMethodName(right));
			System.out.println("}");
		}
	}
	
	public static void endMethod(){
		System.out.println("}\n");
	}
	
	public static String nodeToMethodName(HuffmanNode node){
		/*
		// this works, but doesn't need to be this complicated
		if(node.isLeaf()){
			return "m_" + node.getBlock().value + "_" + node.value;
		} else {
			return "m_" + node.value + "_" + getIndex(getRoot(node), node);
		}
		*/
		
		return "m_" + getIndex(getRoot(node), node);
	}
	
	public static int getIndex(HuffmanNode root, HuffmanNode node){
		LinkedList<HuffmanNode> queue = new LinkedList<HuffmanNode>();
		queue.addLast(root);
		return getIndex(node, 0, queue);
	}
	
	// performs a BFS to find the node index
	private static int getIndex(HuffmanNode node, int i, LinkedList<HuffmanNode> queue){
		if(queue.isEmpty()){
			return -1;
		} else {
			HuffmanNode current = queue.remove();
			if(current.compareTo(node) == 0){
				return i;
			} else {
				if(current.left != null){
					queue.addLast(current.left);
				}
				if(current.right != null){
					queue.addLast(current.right);
				}
				return getIndex(node, ++i, queue);
			}
		}
	}
	
	public static HuffmanNode getRoot(HuffmanNode node){
		if(node.parent == null){
			return node;
		} else {
			return getRoot(node.parent);
		}
	}

}

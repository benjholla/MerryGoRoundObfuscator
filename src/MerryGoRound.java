import huffman.Block;
import huffman.Huffman;
import huffman.HuffmanNode;
import huffman.HuffmanTree;

import java.io.File;
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
		int blockSize = 1;
		
		Map<Block, Integer> frequencies = Huffman.getBlockFrequencies(file, blockSize);
		System.out.println("Block frequencies:\n" + frequencies + "\n");
		
		HuffmanTree tree = Huffman.getHuffmanTree(frequencies, blockSize);
		
		Map<Block,String> encodingKey = Huffman.getEncodingKey(tree);
		System.out.println("Encoding key:\n" + encodingKey + "\n");
		
		int excess = Huffman.compress(file, new File(file.getAbsolutePath() + ".huff"), tree);
		System.out.println("Excess: " + excess + "\n\n");
		
		// create call graph decoder
		framework(tree, blockSize);
		constructCallGraph(tree.root);
	}
	
	// performs a DFS on the huffman tree, while creating the call graph of the static decompressor
	public static void constructCallGraph(HuffmanNode node){
		startMethod(nodeToMethodName(node));
		if(node.isLeaf()){
			writeByte(node.getBlock().value);
		} else {
			writeCondition(node.left, node.right);
		}
		endMethod();
		
		
		if(node.left != null){
			constructCallGraph(node.left);
		}
		if(node.right != null){
			constructCallGraph(node.right);
		}
	}
	
	public static void framework(HuffmanTree tree, int blockSize){
		System.out.println("static FileInputStream si = null;");
		System.out.println("static FileOutputStream so = null;");
		System.out.println("static int ii = 0;");
		System.out.println("static int ou = 0;");
		System.out.println("static int bs = " + blockSize + ";");
		System.out.println("static byte[] bi = new byte[1];");
		System.out.println("static byte[] bo = new byte[1];");
		System.out.println("");
		
		System.out.println("// args[0] = input stream");
		System.out.println("// args[1] = output stream");
		System.out.println("public static void main(String args[]) throws IOException {");
		System.out.println("si = new FileInputStream(new File(args[0]));");
		System.out.println("si.read(bi);");
		System.out.println("so = new FileOutputStream(new File(args[1]));");
		callMethod(nodeToMethodName(tree.root));
		System.out.println("si.close();");
		System.out.println("so.close();");
		System.out.println("}");
		System.out.println("");

		System.out.println("public static byte readByte() throws IOException {");
		System.out.println("if(ii == 0x80){");
		System.out.println("si.read(bi);");
		System.out.println("ii = 0;");
		System.out.println("}");
		System.out.println("return (byte)(bi[0] >> (ii+=bs));");
		System.out.println("}");
		System.out.println("");
		
		System.out.println("public static void writeByte(byte b) throws IOException {");
		System.out.println("ou += bs;");
		System.out.println("bo[0] = (byte)((byte)(bo[0] << bs) | b);");
		System.out.println("if(ou == 0x80){");
		System.out.println("so.write(bo[0]);");
		System.out.println("bo[0] = 0x00;");
		System.out.println("ou = 0;");
		System.out.println("}");
		System.out.println("}");
		System.out.println("");
	}
	
	public static void startMethod(String name){
		System.out.println("public static void " + name + "() throws IOException {");
	}
	
	public static void callMethod(String name){
		System.out.println(name + "();");
	}
	
	public static void writeByte(byte b){
		System.out.println("byte b = (byte)0x" + String.format("%02X", b)  + ";");
		System.out.println("writeByte(b);");
	}
	
	public static void writeCondition(HuffmanNode left, HuffmanNode right)
	{
		System.out.println("if(readByte() % 2 == 0){");
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

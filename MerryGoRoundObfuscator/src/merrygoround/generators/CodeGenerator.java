package merrygoround.generators;

import merrygoround.huffman.HuffmanNode;
import merrygoround.huffman.HuffmanTree;

public class CodeGenerator {

	// performs a DFS on the huffman tree, while creating the call graph of the static decompressor
	public static void constructCallGraph(HuffmanNode node, boolean useRecursion){
		startMethod(nodeToMethodName(node));
		if(node.isLeaf()){
			writeByte(node.getBlock().value);
			if(useRecursion){
				callMethod(nodeToMethodName(HuffmanTree.getRoot(node)));
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
	
	public static void readFromFileFramework(HuffmanTree tree, int blockSize, long fileSize, boolean useRecursion){
		System.out.println("private static RandomAccessFile is = null;");
		System.out.println("private static BufferedOutputStream os = null;");
		System.out.println("private static int bs = " + blockSize + ";");
		System.out.println("private static int ii = -1;");
		System.out.println("private static int oi = bs;");
		System.out.println("private static byte[] bi = new byte[1];");
		System.out.println("private static byte[] bo = new byte[1];");
		System.out.println("private static long fs = " + fileSize + ";");
		System.out.println("private static long fso = 0;");
		System.out.println("");
		
		System.out.println("// args[0] = input stream");
		System.out.println("// args[1] = output stream");
		System.out.println("public static void main(String args[]) throws IOException {");
		System.out.println("is = new RandomAccessFile(new File(args[0]), \"r\");");
		System.out.println("os = new BufferedOutputStream(new FileOutputStream(new File(args[1])));");
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
		System.out.println("is.close();");
		System.out.println("os.close();");
		System.out.println("}");
		System.out.println("}");
		System.out.println("");

		System.out.println("private static byte read() throws IOException {");
		System.out.println("if (ii < 0) {");
		System.out.println("bi[0] = is.readByte();");
		System.out.println("ii = 7;");
		System.out.println("}");
		System.out.println("byte result = (byte) ((bi[0] & 0xFF) >> ii);");
		System.out.println("ii--;");
		System.out.println("return result;");		
		System.out.println("}");
		System.out.println("");
		
		System.out.println("private static void write(byte b) throws IOException {");
		System.out.println("if(fso < fs){");
		System.out.println("bo[0] = (byte) (bo[0] | (b << (8 - oi)));");
		System.out.println("oi += bs;");
		System.out.println("if (oi > 8) {");
		System.out.println("os.write(bo[0]);");
		System.out.println("fso++;");
		System.out.println("bo[0] = 0x00;");
		System.out.println("oi = bs;");
		System.out.println("}");
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
		
		return "m_" + HuffmanTree.getIndex(HuffmanTree.getRoot(node), node);
	}
	
}

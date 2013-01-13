import huffman.HuffmanNode;
import huffman.HuffmanTree;


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
		
		// create call graph decoder
		/*
		framework(tree, blockSize);
		dfs(tree.root);
		*/
	}
	
	// performs a DFS on the huffman tree 
	public static void dfs(HuffmanNode node){
		startMethod(nodeToMethodName(node));
		if(node.isLeaf()){
			writeByte(node.getBlock().value);
		} else {
			writeCondition(node.left, node.right);
		}
		endMethod();
		
		
		if(node.left != null){
			dfs(node.left);
		}
		if(node.right != null){
			dfs(node.right);
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
		if(node.getBlock() != null){
			return "m_" + node.getBlock().value + "_" + node.value;
		} else {
			return "m_" + node.value;
		}
	}

}

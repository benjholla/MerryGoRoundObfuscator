package huffman;

public class HuffmanNode implements Comparable<HuffmanNode> {

	public HuffmanNode parent;
	public HuffmanNode left;
	public HuffmanNode right;
	public int value;
	private Block block;
	private boolean isLeaf;

	// used for leaves
	public HuffmanNode(HuffmanNode parent, HuffmanNode left, HuffmanNode right, Block block, int value) {
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.block = block;
		this.value = value;
		this.isLeaf = true;
	}
	
	// used for branch nodes
	public HuffmanNode(HuffmanNode parent, HuffmanNode left, HuffmanNode right, int value){
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.block = null;
		this.value = value;
		this.isLeaf = false;
	}
	
	public Block getBlock(){
		return block;
	}
	
	public boolean isLeaf(){
		return isLeaf;
	}

	@Override
	public String toString() {
		if(block != null){
			return "" + (char)((byte) block.getValue()) + " : " + value;
		} else {
			return "" + value;
		}
	}

	@Override
	public int compareTo(HuffmanNode node) {
		if(this.value - node.value == 0 && block != null){
			return this.block.getValue() - node.block.getValue() ;
		} else {
			return this.value - node.value;
		}
	}

}

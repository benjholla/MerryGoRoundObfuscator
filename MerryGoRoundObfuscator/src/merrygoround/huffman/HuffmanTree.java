package merrygoround.huffman;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;


public class HuffmanTree {

	public HuffmanNode root;
	public int blockSize;
	
	public HuffmanTree(Map<Block,Integer> frequencies, int blockSize){
		
		this.blockSize = blockSize;
		
		// sort the frequency map and convert entries into a sorted list of HuffmanNodes		
		PriorityQueue<HuffmanNode> frequencySet = new PriorityQueue<HuffmanNode>();

		for(Entry<Block, Integer> entry : frequencies.entrySet()){
			// as a flat set the Huffman node doesn't have a root, left, or right node yet
			frequencySet.add(new HuffmanNode(null, null, null, entry.getKey(), entry.getValue()));
		}
		
		// debug (NOTE: PriorityQueue iterator is not guaranteed to iterate in order!)
		//System.out.println(frequencySet + "\n");
		
		// build the Huffman tree
		while(frequencySet.size() >= 2){
			HuffmanNode left = frequencySet.remove();
			frequencySet.remove(left);
			HuffmanNode right = frequencySet.remove();
			frequencySet.remove(right);
			HuffmanNode node = new HuffmanNode(null, left, right, left.value + right.value);
			left.parent = node;
			right.parent = node;
			frequencySet.add(node);
			
			// debug
			//System.out.println("New Node: " + node.value + ", left " + left.value + ", right " + right.value);
		}
		
		// should only be one node left in the set now
		root = frequencySet.remove();
		
		// debug
		//System.out.println("Root: " + root.value);
	}
	
	//  performs a BFS to find the node index
	public static int getIndex(HuffmanNode root, HuffmanNode node){
		LinkedList<HuffmanNode> queue = new LinkedList<HuffmanNode>();
		queue.addLast(root);
		return getIndex(node, 0, queue);
	}
	
	// private helper method for getIndex
	private static int getIndex(HuffmanNode node, int i, LinkedList<HuffmanNode> queue){
		if(queue.isEmpty()){
			return -1;
		} else {
			HuffmanNode current = queue.remove();
			if(current.isIdentical(node)){
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
	
	// returns the root for a Huffman tree of a starting from a particular node
	public static HuffmanNode getRoot(HuffmanNode node){
		if(node.parent == null){
			return node;
		} else {
			return getRoot(node.parent);
		}
	}
	
}

package huffman;
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
	
}

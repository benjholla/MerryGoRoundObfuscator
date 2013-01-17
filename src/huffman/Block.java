package huffman;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Block {

	public int blockSize;
	public byte mask;
	public byte value;

	public Block(int blockSize) {
		this.blockSize = blockSize;
		
		// minus one for not counting zero
		int range = (int) Math.pow(2, blockSize) - 1;

		// convert int value to byte
		this.mask = (byte) (range & 0xFF);
	}
	
	// the value of byte is automatically masked
	public Block(int blockSize, byte value) {
		this(blockSize);
		setValue(value);
	}
	
	// the value of byte is automatically masked based on blockSize
	public void setValue(byte value){
		this.value = (byte) (value & mask);
	}
	
	public Byte getValue(){
		return new Byte(value);
	}
	
	public String getStringValue(){
        return byteToBinaryString(value).substring(8 - blockSize, 8);
	}
	
	@Override
	public String toString(){
		return getStringValue();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Block){
			if(this.blockSize == ((Block)obj).blockSize){
				if(this.value == ((Block)obj).value){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean equals(int blockSize, byte value) {
		Block block = new Block(blockSize, value);
		return this.equals(block);
	}

	@Override
	public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(blockSize).
            append(value).
            toHashCode();
    }
	
	// ------------------- static helpers for blocks and bytes  ------------------------
	
	public static String byteToBinaryString(byte b){
		String result = ""; 
        byte[] reference = new byte[]{(byte) 0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01}; 
        for (int i = 0; i < 8; i++) { 
            if ((reference[i] & b) != 0) { 
            	result += "1"; 
            } 
            else { 
            	result += "0"; 
            } 
        }
        return result;
	}
	
	public static String byteToHexString(byte b) {
		return "0x" + String.format("%02x", b).toUpperCase();
	}
	
	// reverse the order of a byte's bits
	public static byte reverse(byte b){
		byte result = 0x00;
		for(int i=0; i<8; i++){
			if(((b & 0xFF) >> i) % 2 != 0){
				result |= (byte)(0x01 << (7-i));
			}
		}
		return result;
	}
	
	// sets a single bit of byte b to 1 for specified index
	// index range is 0-7, where 7 is the left most bit and 0 is the right most bit
	public static byte setBit(byte b, int index){
		return (byte)(b | (0x01 << index));
	}
	
}

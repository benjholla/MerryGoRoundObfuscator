package sample.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Test3 {

	static RandomAccessFile si = null;
	static BufferedOutputStream so = null;
	static int ii = 0;
	static int oi = 0;
	static int bs = 1;
	static byte[] bi = new byte[1];
	static byte[] bo = new byte[1];

	// args[0] = input stream
	// args[1] = output stream
	public static void main(String args[]) throws IOException {
		si = new RandomAccessFile(new File("/Users/benjholla/Desktop/huffman.txt.huff"), "r");
		so = new BufferedOutputStream(new FileOutputStream(new File("/Users/benjholla/Desktop/huffman.txt.huff.txt")));
		try {
			m_0();
		}
		catch (Exception e){
			// end of file..
		} finally {
			si.close();
			so.close();
		}
	}

	public static byte readByte() throws IOException {
		if (ii == 0) {
			bi[0] = si.readByte();
			ii = 8;
			// debug
			//System.out.println("Read Byte: " + Block.byteToBinaryString(bi) + " (" + Block.byteToHexString(bi) + ")");
		}
		byte result = (byte)((bi[0] & 0xFF) >> (ii-1));
		// debug
		// System.out.println("Result Byte: " + Block.byteToBinaryString(result) + " (" + Block.byteToHexString(result) + ")");
		ii -= bs;
		return result;
	}

	public static void writeByte(byte b) throws IOException {
		// debug
		// System.out.println("Set " + b % 2 + ", index " + (8 - oi));
		bo[0] = (byte)(bo[0] | (b << (7-oi)));
		
		// debug
		//System.out.println("Updated Byte: " + Block.byteToBinaryString(bo[0]) + " (" + Block.byteToHexString(bo[0]) + ")");
		
		oi += bs;
		if(oi == 8){
			
			// debug
			// System.out.println("Wrote Byte: " + Block.byteToBinaryString(bo[0]) + " (" + Block.byteToHexString(bo[0]) + ")");
			so.write(bo[0]);
			bo[0] = 0x00;
			oi = 0;
		}
	}
	
	public static void m_0() throws IOException {
		if (readByte() % 2 == 0) {
			m_1(); // if 0 write 1
		} else {
			m_2(); // if 1 write 0
		}
	}

	public static void m_1() throws IOException {
		byte b = (byte) 0x01;
		writeByte(b);
		m_0();
	}

	public static void m_2() throws IOException {
		byte b = (byte) 0x00;
		writeByte(b);
		m_0();
	}

}

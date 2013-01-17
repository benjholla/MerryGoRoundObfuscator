package sample.output;
import huffman.Block;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Test2 {

	static RandomAccessFile si = null;
	static FileOutputStream so = null;
	static int ii = 8;
	static int oi = 0;
	static int bs = 1;
	//static byte[] bi = new byte[1];
	static byte[] bo = new byte[1];
	static byte bi;

	// args[0] = input stream
	// args[1] = output stream
	public static void main(String args[]) throws IOException {
		si = new RandomAccessFile(new File("/Users/benjholla/Desktop/huffman.txt.huff"), "r");
		so = new FileOutputStream(new File("/Users/benjholla/Desktop/huffman.txt.huff.txt"));
		readByte();
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
		if (ii >= 7) {
			bi = si.readByte();
			ii = 0;
			System.out.println("Read Byte: " + Block.byteToBinaryString(bi) + "(" + Block.byteToHexString(bi) + ")");
		}
		System.out.println("Input Index = " + (ii + bs));
		return (byte) (bi >> (ii += bs));
	}

	public static void writeByte(byte b) throws IOException {
		oi += bs;
		bo[0] = (byte) ((byte) (bo[0] << bs) | b);
		if (oi == 7) {
			so.write(bo[0]);
			bo[0] = 0x00;
			oi = 0;
			System.out.println("Wrote Byte: " + Block.byteToBinaryString(bo[0]) + "(" + Block.byteToHexString(bo[0]) + ")");
		}
		System.out.println("Output Index = " + ii);
	}

	public static void m_0() throws IOException {
		if (readByte() % 2 == 0) {
			m_1();
		} else {
			m_2();
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

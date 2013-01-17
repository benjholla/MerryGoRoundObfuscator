package sample.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Test4 {

	private static RandomAccessFile si = null;
	private static BufferedOutputStream so = null;
	private static int ii = 0;
	private static int oi = 0;
	private static int bs = 2;
	private static byte[] bi = new byte[1];
	private static byte[] bo = new byte[1];

	// args[0] = input stream
	// args[1] = output stream
	public static void main(String args[]) throws IOException {
		si = new RandomAccessFile(new File(
				"/Users/benjholla/Desktop/huffman.txt.huff"), "r");
		so = new BufferedOutputStream(new FileOutputStream(new File(
				"/Users/benjholla/Desktop/huffman.txt.huff.txt")));
		try {
			while (true) {
				m_0();
			}
		} catch (Exception e) {
			// invalid checksum
		} finally {
			si.close();
			so.close();
		}
	}

	private static byte read() throws IOException {
		if (ii == 0) {
			bi[0] = si.readByte();
			ii = 8;
		}
		byte result = (byte) ((bi[0] & 0xFF) >> (ii - 1));
		ii -= bs;
		return result;
	}

	private static void write(byte b) throws IOException {
		bo[0] = (byte) (bo[0] | (b << (7 - oi)));
		oi += bs;
		if (oi == 8) {
			so.write(bo[0]);
			bo[0] = 0x00;
			oi = 0;
		}
	}

	private static void m_0() throws IOException {
		if (read() % 2 == 0) {
			m_1();
		} else {
			m_2();
		}
	}

	private static void m_1() throws IOException {
		byte b = (byte) 0x01;
		write(b);
	}

	private static void m_2() throws IOException {
		if (read() % 2 == 0) {
			m_3();
		} else {
			m_4();
		}
	}

	private static void m_3() throws IOException {
		if (read() % 2 == 0) {
			m_5();
		} else {
			m_6();
		}
	}

	private static void m_5() throws IOException {
		byte b = (byte) 0x02;
		write(b);
	}

	private static void m_6() throws IOException {
		byte b = (byte) 0x03;
		write(b);
	}

	private static void m_4() throws IOException {
		byte b = (byte) 0x00;
		write(b);
	}

}

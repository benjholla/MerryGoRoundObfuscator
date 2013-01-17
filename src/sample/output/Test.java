package sample.output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

	static FileInputStream si = null;
	static FileOutputStream so = null;
	static int ii = 0;
	static int ou = 0;
	static int bs = 8;
	static byte[] bi = new byte[1];
	static byte[] bo = new byte[1];

	// args[0] = input stream
	// args[1] = output stream
	public static void main(String args[]) throws IOException {
		si = new FileInputStream(new File("/Users/benjholla/Desktop/huffman.txt.huff"));
		si.read(bi);
		so = new FileOutputStream(new File("/Users/benjholla/Desktop/huffman.txt.huff.txt"));
		try {
			while(true){
				m_0();
			}
		} finally {
			si.close();
			so.close();
		}
	}

	public static byte readByte() throws IOException {
		if (ii == 0x80) {
			si.read(bi);
			ii = 0;
		}
		return (byte) (bi[0] >> (ii += bs));
	}

	public static void writeByte(byte b) throws IOException {
		ou += bs;
		bo[0] = (byte) ((byte) (bo[0] << bs) | b);
		if (ou == 0x80) {
			so.write(bo[0]);
			bo[0] = 0x00;
			ou = 0;
		}
	}

	public static void m_0() throws IOException {
		if (readByte() % 2 == 0) {
			m_1();
		} else {
			m_2();
		}
	}

	public static void m_1() throws IOException {
		if (readByte() % 2 == 0) {
			m_3();
		} else {
			m_4();
		}
	}

	public static void m_3() throws IOException {
		byte b = (byte) 0x69;
		writeByte(b);
	}

	public static void m_4() throws IOException {
		byte b = (byte) 0x74;
		writeByte(b);
	}

	public static void m_2() throws IOException {
		if (readByte() % 2 == 0) {
			m_5();
		} else {
			m_6();
		}
	}

	public static void m_5() throws IOException {
		byte b = (byte) 0x20;
		writeByte(b);
	}

	public static void m_6() throws IOException {
		byte b = (byte) 0x73;
		writeByte(b);
	}

}

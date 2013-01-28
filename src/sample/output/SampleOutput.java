package sample.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SampleOutput {

	private static RandomAccessFile is = null;
	private static BufferedOutputStream os = null;
	private static int bs = 8;
	private static int ii = -1;
	private static int oi = bs;
	private static byte[] bi = new byte[1];
	private static byte[] bo = new byte[1];
	private static long fs = 15;
	private static long fso = 0;

	// args[0] = input stream
	// args[1] = output stream
	public static void main(String args[]) throws IOException {
		is = new RandomAccessFile(new File(args[0]), "r");
		os = new BufferedOutputStream(new FileOutputStream(new File(args[1])));
		try {
			m_0();
		} catch (Exception e) {
			// invalid checksum
		} finally {
			is.close();
			os.close();
		}
	}

	private static byte read() throws IOException {
		if (ii < 0) {
			bi[0] = is.readByte();
			ii = 7;
		}
		byte result = (byte) ((bi[0] & 0xFF) >> ii);
		ii--;
		return result;
	}

	private static void write(byte b) throws IOException {
		if (fso < fs) {
			bo[0] = (byte) (bo[0] | (b << (8 - oi)));
			oi += bs;
			if (oi > 8) {
				os.write(bo[0]);
				fso++;
				bo[0] = 0x00;
				oi = bs;
			}
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
		if (read() % 2 == 0) {
			m_3();
		} else {
			m_4();
		}
	}

	private static void m_3() throws IOException {
		if (read() % 2 == 0) {
			m_7();
		} else {
			m_8();
		}
	}

	private static void m_7() throws IOException {
		byte b = (byte) 0x68;
		write(b);
		m_0();
	}

	private static void m_8() throws IOException {
		if (read() % 2 == 0) {
			m_13();
		} else {
			m_14();
		}
	}

	private static void m_13() throws IOException {
		byte b = (byte) 0x61;
		write(b);
		m_0();
	}

	private static void m_14() throws IOException {
		byte b = (byte) 0x65;
		write(b);
		m_0();
	}

	private static void m_4() throws IOException {
		byte b = (byte) 0x73;
		write(b);
		m_0();
	}

	private static void m_2() throws IOException {
		if (read() % 2 == 0) {
			m_5();
		} else {
			m_6();
		}
	}

	private static void m_5() throws IOException {
		if (read() % 2 == 0) {
			m_9();
		} else {
			m_10();
		}
	}

	private static void m_9() throws IOException {
		byte b = (byte) 0x69;
		write(b);
		m_0();
	}

	private static void m_10() throws IOException {
		byte b = (byte) 0x74;
		write(b);
		m_0();
	}

	private static void m_6() throws IOException {
		if (read() % 2 == 0) {
			m_11();
		} else {
			m_12();
		}
	}

	private static void m_11() throws IOException {
		if (read() % 2 == 0) {
			m_15();
		} else {
			m_16();
		}
	}

	private static void m_15() throws IOException {
		byte b = (byte) 0x2E;
		write(b);
		m_0();
	}

	private static void m_16() throws IOException {
		byte b = (byte) 0x54;
		write(b);
		m_0();
	}

	private static void m_12() throws IOException {
		byte b = (byte) 0x20;
		write(b);
		m_0();
	}

}

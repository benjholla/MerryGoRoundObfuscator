import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Output {

	static FileInputStream si = null;
	static FileOutputStream so = null;
	static int ii = 0;
	static int ou = 0;
	static int bs = 8;
	static byte[] bi = new byte[1];
	static byte[] bo = new byte[1];

	
	public static byte readByte() throws IOException {
		if(ii == 0x80){
			si.read(bi);
			ii = 0;
		}
		return (byte)(bi[0] >> (ii+=bs));
	}
	
	public static void writeByte(byte b) throws IOException {
		ou += bs;
		bo[0] = (byte)((byte)(bo[0] << bs) | b);
		if(ou == 0x80){
			so.write(bo[0]);
			bo[0] = 0x00;
			ou = 0;
		}
	}
	
	// args[0] = input stream
	// args[1] = output stream
	public static void main(String args[]) throws IOException {
		si = new FileInputStream(new File(args[0]));
		si.read(bi);
		so = new FileOutputStream(new File(args[1]));
		m_10();
		si.close();
		so.close();
	}

	private static void m_10() throws IOException {
		if (readByte() % 2 == 0) {
			m_65_4();
		} else {
			m_6();
		}
	}

	private static void m_65_4() throws IOException {
		byte b = (byte) 0x41;
		writeByte(b);
	}

	private static void m_6() throws IOException {
		if (readByte() % 2 == 0) {
			m_66_3();
		} else {
			m_3();
		}
	}

	private static void m_66_3() throws IOException {
		byte b = (byte) 0x42;
		writeByte(b);
	}

	private static void m_3() throws IOException {
		if (readByte() % 2 == 0) {
			m_68_1();
		} else {
			m_67_2();
		}
	}

	private static void m_68_1() throws IOException {
		byte b = (byte) 0x44;
		writeByte(b);
	}

	private static void m_67_2() throws IOException {
		byte b = (byte) 0x43;
		writeByte(b);
	}

}

package merrygoround.generators;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import merrygoround.huffman.Block;

public class IOGenerator {
	
	// Test Cases
	public static void main(String[] args) throws Exception {
		System.out.println(fileToByteArray(new File("/Users/benjholla/Desktop/test.txt")));
		System.out.println(fileToCharArray(new File("/Users/benjholla/Desktop/test.txt")));
		System.out.println(fileToIntegerArray(new File("/Users/benjholla/Desktop/test.txt")));
		System.out.println(fileToLongArray(new File("/Users/benjholla/Desktop/test.txt")));
		System.out.println();
		System.out.println(fileToDoubleByteArray(new File("/Users/benjholla/Desktop/test.txt"), 1));
		System.out.println(fileToDoubleCharArray(new File("/Users/benjholla/Desktop/test.txt"), 1));
		System.out.println(fileToDoubleIntegerArray(new File("/Users/benjholla/Desktop/test.txt"), 1));
		System.out.println(fileToDoubleLongArray(new File("/Users/benjholla/Desktop/test.txt"), 1));
		System.out.println();
		System.out.println(fileToDoubleByteArray(new File("/Users/benjholla/Desktop/test.txt"), 2));
		System.out.println(fileToDoubleCharArray(new File("/Users/benjholla/Desktop/test.txt"), 2));
		System.out.println(fileToDoubleIntegerArray(new File("/Users/benjholla/Desktop/test.txt"), 2));
		System.out.println(fileToDoubleLongArray(new File("/Users/benjholla/Desktop/test.txt"), 2));
	}
	
	public enum IOGeneratorType {
		BYTE, CHAR, INTEGER, LONG
	}

	public static String fileToByteArray(File file) throws Exception {
		return buildArray(file, IOGeneratorType.BYTE);
	}

	public static String fileToCharArray(File file) throws Exception {
		return buildArray(file, IOGeneratorType.CHAR);
	}

	public static String fileToIntegerArray(File file) throws Exception {
		return buildArray(file, IOGeneratorType.INTEGER);
	}
	
	public static String fileToLongArray(File file) throws Exception {
		return buildArray(file, IOGeneratorType.LONG);
	}
	
	public static String fileToDoubleByteArray(File file, int width) throws Exception {
		return buildDoubleArray(file, IOGeneratorType.BYTE, width);
	}

	public static String fileToDoubleCharArray(File file, int width) throws Exception {
		return buildDoubleArray(file, IOGeneratorType.CHAR, width);
	}
	
	public static String fileToDoubleIntegerArray(File file, int width) throws Exception {
		return buildDoubleArray(file, IOGeneratorType.INTEGER, width);
	}
	
	public static String fileToDoubleLongArray(File file, int width) throws Exception {
		return buildDoubleArray(file, IOGeneratorType.LONG, width);
	}

	// helper methods
	public static ArrayList<String> readFile(File file, IOGeneratorType type) throws Exception {
		ArrayList<String> values = new ArrayList<String>();
		RandomAccessFile raf = new RandomAccessFile(file, "r"); 
		while(raf.getFilePointer() < raf.length()){
			switch (type) {
				case BYTE:
					byte b = raf.readByte();
					values.add(Block.byteToHexString(b));
					break;
				case CHAR:
					Character c = raf.readChar();
					values.add("'\\u" + Integer.toHexString(c | 0x10000).substring(1) + "'");
					break;
				case INTEGER:
					int i = raf.readInt();
					values.add("" + i);
					break;
				case LONG:
					long l = raf.readLong();
					values.add(l + "L");
					break;
				default:
					throw new IllegalArgumentException("Invalid type.");
			}
		}
		raf.close();
		return values;
	}
	
	private static String buildArray(File file, IOGeneratorType type) throws Exception {
		StringBuilder array = new StringBuilder();
		switch (type) {
			case BYTE:
				array.append("byte");
				break;
			case CHAR:
				array.append("char");
				break;
			case INTEGER:
				array.append("int");
				break;
			case LONG:
				array.append("long");
				break;
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
		array.append("[] f = {");
		ArrayList<String> values = readFile(file, type);
		for(String value : values){
			array.append(value + ", ");
		}
		if(values.size() > 0){
			array.delete(array.length()-2, array.length()); // remove extra ", "
		}
		array.append("};");
		return array.toString();
	}
	
	private static String buildDoubleArray(File file, IOGeneratorType type, int width) throws Exception {
		// convert single array to a double array
		ArrayList<String> singleArray = readFile(file, type);
		ArrayList<ArrayList<String>> doubleArray = new ArrayList<ArrayList<String>>();
		while(singleArray.size() >= width){
			ArrayList<String> row = new ArrayList<String>();
			for(int i=0; i<width; i++){
				row.add(singleArray.remove(0));
			}
			doubleArray.add(row);
		}
		
		// add left overs for last row if needed
		if(singleArray.size() > 0){
			
			ArrayList<String> lastrow = new ArrayList<String>();
			while(!singleArray.isEmpty()){
				lastrow.add(singleArray.remove(0));
			}
			doubleArray.add(lastrow);
		}
		
		// build up the string result
		StringBuilder array = new StringBuilder();
		switch (type) {
			case BYTE:
				array.append("byte");
				break;
			case CHAR:
				array.append("char");
				break;
			case INTEGER:
				array.append("int");
				break;
			case LONG:
				array.append("long");
				break;
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
		array.append("[][] f = {");
		for(ArrayList<String> row : doubleArray){
			array.append("{");
			for(String value : row){
				array.append(value + ", ");
			}
			array.delete(array.length()-2, array.length()); // remove extra ", "
			array.append("},");
		}
		if(doubleArray.size() > 0){
			array.delete(array.length()-1, array.length()); // remove extra ", \n"
		}
		array.append("};");
		return array.toString();
	}

}

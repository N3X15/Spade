/**
 * 
 */
package net.nexisonline.spade;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rob
 *
 */
public class SpadeLogging {
	public static Logger log = Logger.getLogger("Minecraft");
	
	public static void info(String message) {
		log.info(message);
	}

	public static void severe(String msg, Exception e) {
		log.log(Level.SEVERE, msg, e);
	}
}

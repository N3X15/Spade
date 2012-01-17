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
    private static boolean debug = false;
    
    public static void info(final String message) {
        log.info(message);
    }
    
    public static void severe(final String msg, final Exception e) {
        log.log(Level.SEVERE, msg, e);
    }
    
    public static void debug(final String format) {
        if (debug) {
            log.log(Level.INFO, null);
        }
    }
    
    public static void setDebugging(final boolean value) {
        debug = value;
    }
    
    public static boolean getDebugging() {
        return debug;
    }
}

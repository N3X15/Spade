package net.nexisonline.spade;

import java.util.HashMap;
import java.util.Map;

public class SpadeConf {
    
    public static int getInt(final Map<String, Object> node, final String key, final int defaultValue) {
        if ((node == null) || !node.containsKey(key))
            return defaultValue;
        return (Integer) node.get(key);
    }
    
    public static String getString(final Map<String, Object> node, final String key, final String defaultValue) {
        if ((node == null) || !node.containsKey(key))
            return defaultValue;
        return (String) node.get(key);
    }
    
    public static double getDouble(final Map<String, Object> node, final String key, final double defaultValue) {
        if ((node == null) || !node.containsKey(key))
            return defaultValue;
        return (Double) node.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNode(final Map<String, Object> node, final String key) {
        if ((node == null) || !node.containsKey(key))
            return new HashMap<String, Object>();
        return (Map<String, Object>) node.get(key);
    }
    
}

package net.nexisonline.spade;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nexisonline.spade.populators.DungeonPopulator;
import net.nexisonline.spade.populators.OrePopulator;
import net.nexisonline.spade.populators.PonyCaveGenerator;
import net.nexisonline.spade.populators.SedimentGenerator;
import net.nexisonline.spade.populators.SpadeEffectGenerator;
import net.nexisonline.spade.populators.StalactiteGenerator;

import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.config.ConfigurationNode;

public class GenerationManager {
    boolean                                     populate             = true;
    List<Class<? extends SpadeEffectGenerator>> knownBlockPopulators = new ArrayList<Class<? extends SpadeEffectGenerator>>();
    List<BlockPopulator>                        populators           = new ArrayList<BlockPopulator>();
    
    @SuppressWarnings("unchecked")
    public GenerationManager(SpadePlugin plugin, String world,
            Map<String, Object> map, long seed) {
        
        if (map.get("populators") == null)
            map.put("populators", getDefaultPopulators());
        for (Object o : (Collection<Object>)map.get("populators")) {
            if (o instanceof Map) {
                Map<String,Object> segNode = (Map<String,Object>) o;
                String populatorName = (String) segNode.get("name");
                SpadeLogging.info("[GM] Current populator: " + populatorName);
                if (!populatorName.isEmpty()) {
                    Class<? extends SpadeEffectGenerator> c;
                    try {
                        c = (Class<? extends SpadeEffectGenerator>) Class.forName(populatorName);
                        if (c == null) {
                            SpadeLogging.severe("Unable to find populator: " + populatorName, null);
                            continue;
                        }
                        Method m = c.getMethod("getInstance", SpadePlugin.class, ConfigurationNode.class, long.class);
                        SpadeEffectGenerator seg = (SpadeEffectGenerator) m.invoke(null, plugin, segNode, seed);
                        populators.add(seg);
                    } catch (Exception e) {
                        SpadeLogging.severe("Unable to load populator " + populatorName, e);
                    }
                }
            }
        }
    }
    
    private List<Object> getDefaultPopulators() {
        List<Object> nodes = new ArrayList<Object>();
        Map<String,Object> currentNode;
        
        // Sediment
        currentNode = new HashMap<String,Object>();
        currentNode.put("name", SedimentGenerator.class.getName());
        nodes.add(currentNode);
        
        // Caves
        currentNode = new HashMap<String,Object>();
        currentNode.put("name", PonyCaveGenerator.class.getName());
        nodes.add(currentNode);
        
        // Ores
        currentNode = new HashMap<String,Object>();
        currentNode.put("name", OrePopulator.class.getName());
        nodes.add(currentNode);
        
        // Stalactites
        currentNode = new HashMap<String,Object>();
        currentNode.put("name", StalactiteGenerator.class.getName());
        nodes.add(currentNode);
        
        // Dungeons
        currentNode = new HashMap<String,Object>();
        currentNode.put("name", DungeonPopulator.class.getName());
        nodes.add(currentNode);
        
        return nodes;
    }
    
    public static Map<String,Object> getConfigNodeFor(SpadeEffectGenerator seg) {
        Map<String,Object> node = seg.getConfiguration();
        node.put("name", seg.getClass().getName());
        return node;
    }
    
    public List<BlockPopulator> getPopulators() {
        return populators;
    }
    
    public List<Object> getConfig() {
        List<Object> nodes = new ArrayList<Object>();
        Integer i = 0;
        for (BlockPopulator pop : populators) {
            nodes.add(getConfigNodeFor((SpadeEffectGenerator) pop));
            i++;
        }
        return nodes;
    }
}

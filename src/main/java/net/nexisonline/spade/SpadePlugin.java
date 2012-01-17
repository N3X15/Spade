package net.nexisonline.spade;

import java.util.HashMap;
import java.util.Map;

import net.nexisonline.spade.chunkproviders.ChunkProviderDoublePerlin;
import net.nexisonline.spade.chunkproviders.ChunkProviderFlatGrass;
import net.nexisonline.spade.chunkproviders.ChunkProviderGemini;
import net.nexisonline.spade.chunkproviders.ChunkProviderMountains;
import net.nexisonline.spade.chunkproviders.ChunkProviderSurrealIslands;
import net.nexisonline.spade.chunkproviders.ChunkProviderWat;
import net.nexisonline.spade.commands.RegenCommand;
import net.nexisonline.spade.commands.SpadeCommand;
import net.nexisonline.spade.commands.TP2WorldCommand;

import org.bukkit.World.Environment;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Sample plugin for Bukkit
 * 
 * @author Dinnerbone
 */
public class SpadePlugin extends JavaPlugin {
    private final SpadeWorldListener worldListener = new SpadeWorldListener(this);
    private final HashMap<String, SpadeChunkProvider> chunkProviders = new HashMap<String, SpadeChunkProvider>();
    public HashMap<String, GenerationLimits> genLimits = new HashMap<String, GenerationLimits>();
    private final HashMap<String, SpadeChunkProvider> assignedProviders = new HashMap<String, SpadeChunkProvider>();
    
    @Override
    public void onEnable() {
        // Register our events
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.WORLD_LOAD, worldListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.WORLD_SAVE, worldListener, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.CHUNK_LOAD, worldListener, Event.Priority.Monitor, this);
        
        // Register our commands
        //getCommand("setworldgen").setExecutor(new SetWorldGenCommand(this));
        getCommand("regen").setExecutor(new RegenCommand(this));
        getCommand("tpw").setExecutor(new TP2WorldCommand(this));
        getCommand("world").setExecutor(new TP2WorldCommand(this));
        getCommand("spade").setExecutor(new SpadeCommand(this));
        
        registerChunkProviders();
        
        // Load World Settings
        worldListener.loadWorlds();
        worldListener.saveWorlds();
        
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        final PluginDescriptionFile pdfFile = getDescription();
        SpadeLogging.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        
    }
    
    private void registerChunkProviders() {
        chunkProviders.put("stock", null);
        chunkProviders.put("flatgrass", new ChunkProviderFlatGrass(this));
        chunkProviders.put("mountains", new ChunkProviderMountains(this));
        chunkProviders.put("islands", new ChunkProviderSurrealIslands(this));
        chunkProviders.put("wat", new ChunkProviderWat(this));
        chunkProviders.put("doubleperlin", new ChunkProviderDoublePerlin(this));
        chunkProviders.put("gemini", new ChunkProviderGemini(this));
    }
    
    @Override
    public void onDisable() {
    }
    
    public Map<String, Object> loadWorld(final String worldName, final long seed, final String cmName, final String cpName, Map<String, Object> map) {
        final SpadeChunkProvider cp = chunkProviders.get(cpName);
        if (cp != null) {
            cp.worldName = worldName;
            
            if (map == null) {
                map = new HashMap<String, Object>();
            }
            cp.onLoad(worldName, seed, map);
        }
        assignedProviders.put(worldName, cp);
        getServer().createWorld(worldName, Environment.NORMAL, seed, cp);
        return map;
    }
    
    public int getChunkRadius(final String worldName) {
        final GenerationLimits gl = genLimits.get(worldName.toLowerCase());
        if (gl == null)
            return Integer.MAX_VALUE;
        
        if (!gl.enabled)
            return Integer.MAX_VALUE;
        
        return gl.distanceSquared;
    }
    
    public SpadeChunkProvider getProviderFor(final String worldName) {
        return assignedProviders.get(worldName);
    }
    
    public boolean getRound(final String worldName) {
        final GenerationLimits gl = genLimits.get(worldName.toLowerCase());
        if (gl == null)
            return true;
        
        if (!gl.enabled)
            return true;
        
        return gl.round;
    }
    
    public boolean shouldGenerateChunk(final String worldName, final int x, final int z) {
        final GenerationLimits gl = genLimits.get(worldName.toLowerCase());
        if (gl == null)
            return true;
        
        if (!gl.enabled)
            return true;
        
        if (gl.round) {
            final long d2 = Math.round(Math.pow(x, 2) + Math.pow(z, 2));
            return (d2 < gl.distanceSquared);
        } else
            return ((x < gl.distance) || (x < -gl.distance) || (z < gl.distance) || (z < -gl.distance));
    }
    
    public String getNameForClass(final SpadeChunkProvider cp) {
        for (final String key : chunkProviders.keySet()) {
            final SpadeChunkProvider ccp = chunkProviders.get(key);
            if (ccp == null) {
                continue;
            }
            if (ccp.getClass().equals(cp.getClass()))
                return key;
        }
        return "stock";
    }
    
}
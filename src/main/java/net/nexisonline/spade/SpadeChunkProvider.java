package net.nexisonline.spade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public abstract class SpadeChunkProvider extends ChunkGenerator {
    
    protected String worldName = "";
    private GenerationManager popmanager = null;
    protected SpadePlugin plugin;
    
    public SpadeChunkProvider(final SpadePlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Load world settings and configure chunk generator
     * @param worldSeed All we know about the new world presently
     * @param map Configuration node for this plugin.
     */
    public void onLoad(final String worldName, final long worldSeed, final Map<String, Object> map) {
        popmanager = new GenerationManager(plugin, worldName, map, worldSeed);
    }
    
    @Override
    public List<BlockPopulator> getDefaultPopulators(final World w) {
        if (popmanager == null)
            return new ArrayList<BlockPopulator>();
        return popmanager.getPopulators();
    }
    
    @Override
    public boolean canSpawn(final World w, final int x, final int z) {
        //return true;
        final int y = w.getHighestBlockYAt(x, z);
        return !w.getBlockAt(x, y, z).isLiquid();
        
    }
    
    protected void setBlockByte(final byte[] blocks, final int x, final int y, final int z, final byte block) {
        blocks[y + ((z * 128) + (x * 128 * 16))] = block;
    }
    
    public Map<String, Object> getConfig() {
        final Map<String, Object> cfg = new HashMap<String, Object>();
        cfg.put("populators", popmanager.getConfig());
        return cfg;
    }
}

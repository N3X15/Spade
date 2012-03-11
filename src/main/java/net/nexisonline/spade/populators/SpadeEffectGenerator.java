package net.nexisonline.spade.populators;

import java.util.Map;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public abstract class SpadeEffectGenerator extends BlockPopulator {
    protected World world;
    protected SpadePlugin plugin;
    protected Map<String, Object> config;
    protected long seed;
    
    public SpadeEffectGenerator(final SpadePlugin plugin, final Map<String, Object> node, final long seed) {
        this.plugin = plugin;
        config = node;
        this.seed = seed;
    }
    
    protected void ensureChunkIsLoaded(final int x, final int z) {
        if (!world.isChunkLoaded(x, z)) {
            world.loadChunk(x, z);
        }
    }
    
    public static SpadeEffectGenerator getInstance(final SpadePlugin plugin, final Map<String, Object> node, final long seed) {
        return null;
    }
    
    public abstract Map<String, Object> getConfiguration();
}

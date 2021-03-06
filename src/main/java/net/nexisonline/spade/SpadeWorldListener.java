package net.nexisonline.spade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.nexisonline.spade.populators.DungeonPopulator;
import net.nexisonline.spade.populators.OrePopulator;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.generator.BlockPopulator;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

public class SpadeWorldListener implements Listener {
    private final SpadePlugin spade;
    private Collection<String> worlds;
    private Map<String, Object> root;
    private Yaml yaml;
    
    public SpadeWorldListener(final SpadePlugin plugin) {
        spade = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(final ChunkLoadEvent e) {
        for (final BlockPopulator bp : e.getWorld().getPopulators()) {
            if (bp instanceof DungeonPopulator) {
                ((DungeonPopulator) bp).onChunkLoad(e.getChunk().getX(), e.getChunk().getZ());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(final WorldLoadEvent e) {
        final World w = e.getWorld();
        SpadeLogging.info("onWorldLoad: " + w.getName());
        for (final BlockPopulator p : w.getPopulators()) {
            if (p instanceof OrePopulator) {
                ((OrePopulator) p).onWorldLoaded(w);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldSave(final WorldSaveEvent e) {
        saveWorlds();
    }
    
    @SuppressWarnings("unchecked")
    private void load() {
        final DumperOptions options = new DumperOptions();
        
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
        
        FileInputStream stream = null;
        
        final File file = new File(spade.getDataFolder(), "Spade.yml");
        if (!file.exists())
            return;
        
        try {
            stream = new FileInputStream(file);
            root = (Map<String, Object>) yaml.load(new UnicodeReader(stream));
        } catch (final IOException e) {
            root = new HashMap<String, Object>();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (final IOException e) {
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public void loadWorlds() {
        load();
        Object co = null;
        if ((root != null) && root.containsKey("worlds")) {
            co = root.get("worlds");
            if (co instanceof Map<?, ?>) {
                final Map<String, Object> worldMap = (Map<String, Object>) co;
                worlds = worldMap.keySet();
                SpadeLogging.info("Loaded worlds:");
                for (final String worldName : worlds) {
                    final Map<String, Object> currWorld = (Map<String, Object>) worldMap.get(worldName);
                    final Map<String, Object> limits = (Map<String, Object>) currWorld.get("limits");
                    final Long seed = (Long) ((currWorld.get("seed") == null) ? ((new Random()).nextLong()) : currWorld.get("seed"));
                    spade.genLimits.put(worldName.toLowerCase(), new GenerationLimits(limits));
                    
                    Map<String, Object> chunkManager = (Map<String, Object>) currWorld.get("chunk-manager");
                    if (chunkManager == null) {
                        chunkManager = new HashMap<String, Object>();
                        chunkManager.put("name", "stock");
                    }
                    Map<String, Object> chunkProvider = (Map<String, Object>) currWorld.get("chunk-provider");
                    if (chunkProvider == null) {
                        chunkProvider = new HashMap<String, Object>();
                        chunkProvider.put("name", "stock");
                    }
                    SpadeLogging.info(" + " + worldName + " (cp: " + (String) chunkProvider.get("name") + ")");
                    spade.loadWorld(worldName, seed, (String) chunkManager.get("name"), (String) chunkProvider.get("name"), (Map<String, Object>) chunkProvider.get("config"));
                }
            }
        } else {
            root = new HashMap<String, Object>();
            for (final World w : spade.getServer().getWorlds()) {
                final String worldName = w.getName();
                final Map<String, Object> world = new HashMap<String, Object>();
                {
                    final Map<String, Object> chunkProvider = new HashMap<String, Object>();
                    chunkProvider.put("name", "stock");
                    chunkProvider.put("config", null);
                    world.put("chunk-provider", chunkProvider);
                }
                {
                    world.put("limits", (new GenerationLimits()).getConfig());
                }
                root.put(worldName, world);
            }
            save();
        }
    }
    
    private void save() {
        FileOutputStream stream = null;
        final File file = new File(spade.getDataFolder(), "Spade.yml");
        
        final File parent = file.getParentFile();
        
        if (parent != null) {
            parent.mkdirs();
        }
        
        try {
            stream = new FileOutputStream(file);
            final OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
            writer.append("\r\n# Spade Terrain Generator Plugin");
            writer.append("\r\n#   Configuration File");
            writer.append("\r\n# ");
            writer.append("\r\n# AUTOMATICALLY GENERATED");
            writer.append("\r\n");
            
            yaml.dump(root, writer);
            return;
        } catch (final IOException e) {
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (final IOException e) {
            }
        }
        
        return;
    }
    
    public void saveWorlds() {
        root.clear();
        final Map<String, Object> worlds = new HashMap<String, Object>();
        for (final World w : spade.getServer().getWorlds()) {
            final String worldName = w.getName();
            final Map<String, Object> world = new HashMap<String, Object>();
            {
                final Map<String, Object> chunkProvider = new HashMap<String, Object>();
                if (w.getGenerator() instanceof SpadeChunkProvider) {
                    final SpadeChunkProvider cp = (SpadeChunkProvider) w.getGenerator();
                    chunkProvider.put("name", spade.getNameForClass(cp));
                    chunkProvider.put("config", cp.getConfig());
                } else {
                    chunkProvider.put("name", "stock");
                }
                world.put("chunk-provider", chunkProvider);
            }
            {
                spade.genLimits.get(worldName.toLowerCase());
                world.put("limits", (new GenerationLimits()).getConfig());
            }
            worlds.put(worldName, world);
        }
        root.put("worlds", worlds);
        save();
    }
}

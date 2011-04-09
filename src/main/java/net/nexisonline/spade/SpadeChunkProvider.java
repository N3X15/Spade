package net.nexisonline.spade;

import org.bukkit.ChunkProvider;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeChunkProvider extends ChunkProvider {

	protected String worldName="";

	public void setWorldName(String cpName) {
		this.worldName=cpName;
	}

	public abstract ConfigurationNode configure(ConfigurationNode node);
	
}

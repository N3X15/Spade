package net.nexisonline.spade.commands;

import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpadeCommand implements CommandExecutor {
    private static final String prefix = ChatColor.GREEN + "[Spade]" + ChatColor.YELLOW + " ";
    
    public SpadeCommand(final SpadePlugin spadePlugin) {
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] arg) {
        if (arg.length == 0)
            return false;
        
        if (arg[1].equalsIgnoreCase("debug")) {
            if (arg.length == 2) {
                SpadeLogging.setDebugging(arg[2].equalsIgnoreCase("on"));
            }
            sender.sendMessage(prefix + " Debugging is " + (SpadeLogging.getDebugging() ? "on" : "off") + ".");
            return true;
        }
        return false;
    }
    
}

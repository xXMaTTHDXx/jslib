package io.matthd.jslib.external.lib;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Created by Matt on 2017-01-19.
 */
public class LibCommandExecutor implements CommandExecutor {

    private Plugin plugin;
    private LibCommandCallback callback;

    public LibCommandExecutor(Plugin plugin, LibCommandCallback callback) {
        this.plugin = plugin;
        this.callback = callback;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return callback.callback(sender, label, args);
    }
}

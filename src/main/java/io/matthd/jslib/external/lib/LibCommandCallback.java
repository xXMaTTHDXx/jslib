package io.matthd.jslib.external.lib;

import org.bukkit.command.CommandSender;

/**
 * Created by Matt on 2017-01-19.
 */
public interface LibCommandCallback {

    public boolean callback(CommandSender sender, String command, String[] args);
}

package io.matthd.jslib.systems;

import io.matthd.jslib.external.bukkit.Bukkit;

/**
 * Created by Matt on 16/01/2017.
 */
public interface SystemContext {

    public Bukkit getBukkit();

    public DatabaseManager getDatabaseManager();
}

package io.matthd.jslib.external.bukkit;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import sun.font.AttributeMap;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Matt on 16/01/2017.
 */
public interface Bukkit {

    public World getWorld(String var1);

    public Collection<World> getWorlds();

    public Player getPlayer(String var1);

    public Player getPlayerLike(String var1);

    public Player getPlayer(UUID var1);

    public Collection<Player> getPlayers();

    @Deprecated
    public Collection<Player> getAsyncPlayers();

    public int getMaxPlayers();

    public AttributeMap getAttributes(Metadatable var1);

    default public Inventory createInventory(InventoryHolder holder, int size) {
        return this.createInventory(holder, size, InventoryType.CHEST.getDefaultTitle());
    }

    public Inventory createInventory(InventoryHolder var1, int var2, String var3);

    default public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return this.createInventory(holder, type, type.getDefaultTitle());
    }

    public Inventory createInventory(InventoryHolder var1, InventoryType var2, String var3);

    default public <T extends Event> void registerEvent(Class<T> eventClass, BukkitEventHandler<T> handler) {
        this.registerEvent(eventClass, EventPriority.NORMAL, handler);
    }

    default public <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, BukkitEventHandler<T> handler) {
        this.registerEvent(eventClass, priority, true, handler);
    }

    public <T extends Event> void registerEvent(Class<T> var1, EventPriority var2, boolean var3, BukkitEventHandler<T> var4);

    public void dispatchEvent(Event var1);

    default public void registerCommand(String id, BukkitCommandHandler handler) {
        this.registerCommand(id, true, handler);
    }

    public void registerCommand(String var1, boolean var2, BukkitCommandHandler var3);

    public void aliasCommand(String var1, String var2);

    public boolean dispatchCommand(String var1);

    public boolean dispatchCommand(CommandSender var1, String var2);

    public BukkitTask runSync(Runnable var1);

    public BukkitTask runAsync(Runnable var1);

    public BukkitTask runSyncLater(Runnable var1, long var2);

    public BukkitTask runAsyncLater(Runnable var1, long var2);

    default public BukkitTask runSyncTimer(Runnable runnable, long periodTicks) {
        return this.runSyncTimer(runnable, periodTicks, periodTicks);
    }

    default public BukkitTask runAsyncTimer(Runnable runnable, long periodTicks) {
        return this.runAsyncTimer(runnable, periodTicks, periodTicks);
    }

    public BukkitTask runSyncTimer(Runnable var1, long var2, long var4);

    public BukkitTask runAsyncTimer(Runnable var1, long var2, long var4);

    public void shutdown();

    public Scoreboard getScoreboard();

    public BukkitExtended getExtended();

    public Bukkit scope();

    public Bukkit scope(String var1);

    public Server raw();

    public Plugin rawPlugin();
}

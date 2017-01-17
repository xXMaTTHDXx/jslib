/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandMap
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventException
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.metadata.Metadatable
 *  org.bukkit.plugin.EventExecutor
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPluginLoader
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 *  org.spigotmc.CustomTimingsHandler
 */
package io.matthd.jslib.external.bukkit;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import ninja.coelho.arkjs.extern.bukkit.AsyncPlayersList;
import ninja.coelho.arkjs.extern.bukkit.AttributeGlobal;
import ninja.coelho.arkjs.extern.bukkit.AttributeKey;
import ninja.coelho.arkjs.extern.bukkit.AttributeMap;
import ninja.coelho.arkjs.extern.bukkit.Bukkit;
import ninja.coelho.arkjs.extern.bukkit.BukkitCommandHandler;
import ninja.coelho.arkjs.extern.bukkit.BukkitEventHandler;
import ninja.coelho.arkjs.extern.bukkit.BukkitExtended;
import ninja.coelho.arkjs.extern.bukkit.BukkitUtil;
import ninja.coelho.arkjs.extern.bukkit.DelegateCommand;
import ninja.coelho.arkjs.extern.bukkit.DummyPlugin;
import ninja.coelho.arkjs.extern.bukkit.FutureBukkitTask;
import ninja.coelho.arkjs.extern.bukkit.N110BukkitExtended;
import ninja.coelho.arkjs.extern.bukkit.N110Scoreboard;
import ninja.coelho.arkjs.extern.bukkit.N18BukkitExtended;
import ninja.coelho.arkjs.extern.bukkit.N18Scoreboard;
import ninja.coelho.arkjs.extern.bukkit.NoopBukkitExtended;
import ninja.coelho.arkjs.extern.bukkit.PlayerAttributeObserver;
import ninja.coelho.arkjs.extern.bukkit.Scoreboard;
import ninja.coelho.arkjs.extern.bukkit.SimpleAttributeGlobal;
import ninja.coelho.arkjs.extern.bukkit.SimpleScoreboard;
import ninja.coelho.arkjs.util.ManagedScheduledService;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.CustomTimingsHandler;

public class SimpleBukkit
implements Bukkit {
    private static final String ATTRIBUTE_NAME = "arkjs";
    private Plugin plugin;
    private Plugin dummyPlugin;
    private String name;
    private Scoreboard scoreboard;
    private BukkitExtended extended;
    private Collection<Player> asyncPlayers;
    private ManagedScheduledService asyncService;
    private AttributeGlobal attributeGlobal;

    public SimpleBukkit(Plugin plugin) {
        this(plugin, null);
    }

    protected SimpleBukkit(Plugin plugin, SimpleBukkit master) {
        this.plugin = plugin;
        this.dummyPlugin = new DummyPlugin(plugin);
        if (master == null) {
            Plugin dummyPlugin = this.dummyPlugin;
            this.dummyPlugin = plugin;
            try {
                try {
                    this.scoreboard = new N18Scoreboard(this.plugin, this);
                }
                catch (Error | Exception error) {
                    this.scoreboard = new N110Scoreboard(this.plugin, this);
                }
            }
            catch (Error | Exception error) {
                System.out.println("[arkjs] " + error.getClass().getName() + " trying NxScoreboard, falling back to SimpleScoreboard");
                this.scoreboard = new SimpleScoreboard(this.plugin, this);
            }
            this.asyncPlayers = new AsyncPlayersList(this);
            this.asyncService = new ManagedScheduledService();
            this.attributeGlobal = new SimpleAttributeGlobal(new PlayerAttributeObserver(this));
            this.registerEvent(PlayerQuitEvent.class, EventPriority.MONITOR, this::clearAttributesOnQuit);
            this.dummyPlugin = dummyPlugin;
        } else {
            this.scoreboard = master.scoreboard;
            this.asyncPlayers = master.asyncPlayers;
            this.asyncService = master.asyncService.copy();
            this.attributeGlobal = master.attributeGlobal;
        }
        try {
            try {
                this.extended = new N18BukkitExtended(this.plugin, this);
            }
            catch (Error | Exception error) {
                this.extended = new N110BukkitExtended(this.plugin, this);
            }
        }
        catch (Error | Exception error) {
            if (master == null) {
                System.out.println("[arkjs] " + error.getClass().getName() + " trying NxBukkitExtended, falling back to noop");
            }
            this.extended = new NoopBukkitExtended();
        }
    }

    protected SimpleBukkit(Plugin plugin, SimpleBukkit master, String name) {
        this(plugin, master);
        this.name = name;
    }

    @Override
    public World getWorld(String name) {
        return this.raw().getWorld(name);
    }

    @Override
    public Collection<World> getWorlds() {
        return this.raw().getWorlds();
    }

    @Override
    public Player getPlayer(String name) {
        return this.raw().getPlayerExact(name);
    }

    @Override
    public Player getPlayerLike(String name) {
        return this.raw().getPlayer(name);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return this.raw().getPlayer(uuid);
    }

    @Override
    public Collection<Player> getPlayers() {
        return this.asyncPlayers;
    }

    @Override
    public <T> Map<Player, T> getPlayersWithAttribute(AttributeKey<T> key) {
        return this.attributeGlobal.getObserver().getWithAttribute(key);
    }

    @Deprecated
    @Override
    public Collection<Player> getAsyncPlayers() {
        return this.asyncPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return this.raw().getMaxPlayers();
    }

    @Override
    public AttributeMap getAttributes(Metadatable metadatable) {
        return this.attributeGlobal.get(metadatable);
    }

    public void clearAttributesOnQuit(PlayerQuitEvent event) {
        this.attributeGlobal.onPlayerQuit(event.getPlayer());
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, int size, String title) {
        return this.raw().createInventory(holder, size, title);
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        return this.raw().createInventory(holder, type, title);
    }

    @Override
    public <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, boolean ignoreCancelled, BukkitEventHandler<T> handler) {
        String handlerName = this.name;
        if (handlerName == null) {
            handlerName = handler.getClass().getName();
        }
        CustomTimingsHandler timings = new CustomTimingsHandler("Plugin: " + this.plugin.getDescription().getFullName() + " Event: " + "ArkJs::" + handlerName + "(" + eventClass.getSimpleName() + ")", JavaPluginLoader.pluginParentTimer);
        this.raw().getPluginManager().registerEvent(eventClass, new Listener(){}, priority, (listener, event) -> {
            if (!eventClass.isInstance((Object)event)) {
                return;
            }
            if (event.isAsynchronous()) {
                handler.handle(event);
            } else {
                timings.startTiming();
                handler.handle(event);
                timings.stopTiming();
            }
        }
        , this.dummyPlugin, ignoreCancelled);
    }

    @Override
    public void dispatchEvent(Event event) {
        this.raw().getPluginManager().callEvent(event);
    }

    @Override
    public void registerCommand(String id, boolean console, BukkitCommandHandler handler) {
        CommandMap commandMap = BukkitUtil.getCommandMap(this.plugin.getServer());
        Map<String, Command> commands = BukkitUtil.getKnownCommands(commandMap);
        commands.put(id, new DelegateCommand(id, this.dummyPlugin, console, handler));
    }

    @Override
    public void aliasCommand(String from, String to) {
        this.registerCommand(from, (sender, args) -> {
            this.raw().dispatchCommand(sender, to + " " + String.join((CharSequence)" ", args));
        }
        );
    }

    @Override
    public boolean dispatchCommand(String command) {
        return this.dispatchCommand((CommandSender)this.plugin.getServer().getConsoleSender(), command);
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String command) {
        return this.raw().dispatchCommand(sender, command);
    }

    @Override
    public BukkitTask runSync(Runnable runnable) {
        return this.raw().getScheduler().runTask(this.dummyPlugin, runnable);
    }

    @Override
    public BukkitTask runSyncLater(Runnable runnable, long delayTicks) {
        return this.raw().getScheduler().runTaskLater(this.dummyPlugin, runnable, delayTicks);
    }

    @Override
    public BukkitTask runSyncTimer(Runnable runnable, long delayTicks, long periodTicks) {
        return this.raw().getScheduler().runTaskTimer(this.dummyPlugin, runnable, delayTicks, periodTicks);
    }

    @Override
    public BukkitTask runAsync(Runnable runnable) {
        return new FutureBukkitTask(this.asyncService.schedule(runnable, 0), this.dummyPlugin, false);
    }

    @Override
    public BukkitTask runAsyncLater(Runnable runnable, long delayTicks) {
        return new FutureBukkitTask(this.asyncService.schedule(runnable, delayTicks * 50), this.dummyPlugin, false);
    }

    @Override
    public BukkitTask runAsyncTimer(Runnable runnable, long delayTicks, long periodTicks) {
        return new FutureBukkitTask(this.asyncService.scheduleAtFixedRate(runnable, delayTicks * 50, periodTicks * 50), this.dummyPlugin, false);
    }

    @Override
    public void shutdown() {
        this.raw().shutdown();
    }

    @Override
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Override
    public BukkitExtended getExtended() {
        return this.extended;
    }

    @Override
    public Bukkit scope() {
        return new SimpleBukkit(this.plugin, this);
    }

    @Override
    public Bukkit scope(String name) {
        return new SimpleBukkit(this.plugin, this, name);
    }

    @Override
    public Server raw() {
        return this.plugin.getServer();
    }

    @Override
    public Plugin rawPlugin() {
        return this.plugin;
    }

    @Override
    public void close() throws Exception {
        CommandMap commandMap = BukkitUtil.getCommandMap(this.plugin.getServer());
        Iterator<Command> commands = BukkitUtil.getKnownCommands(commandMap).values().iterator();
        while (commands.hasNext()) {
            DelegateCommand delegateCommand;
            Command command = commands.next();
            if (!(command instanceof DelegateCommand) || (delegateCommand = (DelegateCommand)command).getPlugin() != this.dummyPlugin) continue;
            commands.remove();
        }
        HandlerList.unregisterAll((Plugin)this.dummyPlugin);
        this.raw().getScheduler().cancelTasks(this.dummyPlugin);
        this.asyncService.cancel();
    }

}


/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ChatComponentText
 *  net.minecraft.server.v1_8_R3.DataWatcher
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityPig
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayInClientCommand
 *  net.minecraft.server.v1_8_R3.PacketPlayInClientCommand$EnumClientCommand
 *  net.minecraft.server.v1_8_R3.PacketPlayOutChat
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction
 *  net.minecraft.server.v1_8_R3.PlayerConnection
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.Metadatable
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package io.matthd.jslib.external.bukkit;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.World;
import ninja.coelho.arkjs.extern.bukkit.AttributeKey;
import ninja.coelho.arkjs.extern.bukkit.AttributeMap;
import ninja.coelho.arkjs.extern.bukkit.Bukkit;
import ninja.coelho.arkjs.extern.bukkit.BukkitExtended;
import ninja.coelho.arkjs.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class ProtocolBukkitExtended
implements BukkitExtended {
    public static final AttributeKey<Integer> barAttribute = new AttributeKey();
    private Plugin plugin;
    private Bukkit bukkit;

    public ProtocolBukkitExtended(Plugin plugin, Bukkit bukkit) {
        this.plugin = plugin;
        this.bukkit = bukkit;
    }

    @Override
    public void respawn(Player player) {
        assert (player.getServer() == this.plugin.getServer());
        if (!player.isDead()) {
            return;
        }
        ((CraftPlayer)player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
    }

    @Override
    public void setTabHeading(Player player, String header, String footer) {
        assert (player.getServer() == this.plugin.getServer());
        CraftPlayer craftPlayer = (CraftPlayer)player;
        PacketPlayOutPlayerListHeaderFooter packetListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter((IChatBaseComponent)new ChatComponentText(header));
        try {
            ReflectionUtil.set((Object)packetListHeaderFooter, "b", (Object)new ChatComponentText(footer));
        }
        catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)packetListHeaderFooter);
    }

    @Override
    public void showTitle(Player player, String title, String subTitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        assert (player.getServer() == this.plugin.getServer());
        CraftPlayer craftPlayer = (CraftPlayer)player;
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTitle(fadeInTicks, stayTicks, fadeOutTicks));
        if (title == null) {
            title = "";
        }
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, (IChatBaseComponent)new ChatComponentText(title)));
        if (subTitle == null) {
            subTitle = "";
        }
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, (IChatBaseComponent)new ChatComponentText(subTitle)));
    }

    @Override
    public void showBar(Player player, String title, int percentage) {
        if (percentage == 0) {
            percentage = 1;
        }
        CraftPlayer craftPlayer = (CraftPlayer)player;
        AttributeMap metadata = this.bukkit.getAttributes((Metadatable)player);
        if (title == null) {
            if (!metadata.containsKey(barAttribute)) {
                return;
            }
            int entityId = metadata.get(barAttribute);
            craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutEntityDestroy(new int[]{entityId}));
            metadata.remove(barAttribute);
        } else {
            DataWatcher dataWatcher = new DataWatcher(null);
            dataWatcher.a(2, (Object)title);
            dataWatcher.a(3, (Object)Byte.valueOf(1));
            dataWatcher.a(10, (Object)title);
            dataWatcher.a(11, (Object)Byte.valueOf(1));
            dataWatcher.a(6, (Object)Float.valueOf((float)percentage * 3.0f));
            dataWatcher.a(17, (Object)0);
            dataWatcher.a(18, (Object)0);
            dataWatcher.a(19, (Object)0);
            dataWatcher.a(20, (Object)1000);
            dataWatcher.a(0, (Object)Byte.valueOf(32));
            Location location = player.getLocation().add(player.getEyeLocation().getDirection().multiply(20));
            if (metadata.containsKey(barAttribute)) {
                int entityId = metadata.get(barAttribute);
                craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutEntityMetadata(entityId, dataWatcher, true));
                craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutEntityTeleport(entityId, location.getBlockX() * 32, location.getBlockY() * 32, location.getBlockZ() * 32, (byte)((int)location.getYaw() * 256 / 360), (byte)((int)location.getPitch() * 256 / 360), false));
            } else {
                int entityId = new EntityPig(null).getId();
                PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving();
                try {
                    ReflectionUtil.set((Object)spawnPacket, "a", entityId);
                    ReflectionUtil.set((Object)spawnPacket, "b", EntityType.WITHER.getTypeId());
                    ReflectionUtil.set((Object)spawnPacket, "c", location.getBlockX() * 32);
                    ReflectionUtil.set((Object)spawnPacket, "d", location.getBlockY() * 32);
                    ReflectionUtil.set((Object)spawnPacket, "e", location.getBlockZ() * 32);
                    ReflectionUtil.set((Object)spawnPacket, "l", (Object)dataWatcher);
                }
                catch (NoSuchFieldException exception) {
                    exception.printStackTrace();
                }
                catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                }
                catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
                craftPlayer.getHandle().playerConnection.sendPacket((Packet)spawnPacket);
                metadata.put(barAttribute, entityId);
            }
        }
    }

    @Override
    public void sendActionBar(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer)player;
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutChat((IChatBaseComponent)new ChatComponentText(message), 2));
    }
}


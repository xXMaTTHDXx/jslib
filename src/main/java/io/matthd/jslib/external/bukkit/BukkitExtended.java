/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package io.matthd.jslib.external.bukkit;

import org.bukkit.entity.Player;

public interface BukkitExtended {
    public void respawn(Player var1);

    public void setTabHeading(Player var1, String var2, String var3);

    default public void showTitle(Player player, String title, String subTitle) {
        this.showTitle(player, title, subTitle, 20, 40, 20);
    }

    public void showTitle(Player var1, String var2, String var3, int var4, int var5, int var6);

    public void showBar(Player var1, String var2, int var3);

    public void sendActionBar(Player var1, String var2);
}


/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 */
package io.matthd.jslib.external.bukkit;

import org.bukkit.event.Event;

public interface BukkitEventHandler<T extends Event> {
    public void handle(T var1);
}


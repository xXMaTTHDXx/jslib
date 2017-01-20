/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 */
package io.matthd.jslib.external.lib;

import org.bukkit.event.Event;

public interface LibEventCallback<T extends Event> {
    public void callback(T var1);
}


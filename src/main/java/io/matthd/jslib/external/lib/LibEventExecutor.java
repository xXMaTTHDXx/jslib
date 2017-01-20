package io.matthd.jslib.external.lib;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

/**
 * Created by Matt on 2017-01-19.
 */
public final class LibEventExecutor<T extends Event> implements EventExecutor, Listener {

    private LibEventCallback<T> callback;
    private Class<T> eventClass;

    public LibEventExecutor(LibEventCallback<T> callback, Class<T> eventClass) {
        this.callback = callback;
        this.eventClass = eventClass;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (eventClass.isInstance(event)) {
            T t = eventClass.cast(event);

            callback.callback(t);
        }
    }
}

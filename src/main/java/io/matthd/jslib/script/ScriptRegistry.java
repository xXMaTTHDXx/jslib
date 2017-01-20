package io.matthd.jslib.script;

import junit.framework.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 2017-01-19.
 */
public class ScriptRegistry {

    private HashMap<Script, File> scripts = new HashMap<>();

    public void register(Script script, File file) {
        this.scripts.put(script, file);
    }

    public void unregisterByScript(Script script) {
        Assert.assertTrue(isRegistered(script));
        scripts.remove(script);
    }

    public boolean isRegistered(Script script) {
        for (Map.Entry entry : scripts.entrySet()) {
            if (entry.getKey() == script) {
                return true;
            }
        }
        return false;
    }
}

package io.matthd.jslib.script;

import javax.script.ScriptEngine;
import java.io.File;

/**
 * Created by Matt on 16/01/2017.
 */
public class Script implements Runnable {

    private static ScriptEngine engine;
    private static String header;
    private static String[] defaultImports;

    private String name;
    private File file;


    public void run() {

    }

    static {

    }
}

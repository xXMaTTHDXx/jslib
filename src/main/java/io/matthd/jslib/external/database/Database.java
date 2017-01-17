package io.matthd.jslib.external.database;

/**
 * Created by Matt on 17/01/2017.
 */
public interface Database {

    public String getHost();
    public int getPort();
    public String getDB();

    public void connect();
}

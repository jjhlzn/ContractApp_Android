package com.jinjunhang.contract.model;

/**
 * Created by lzn on 16/4/30.
 */
public class ServiceLocator {

    public static final String DEFAULT_HTTP = "http";
    public static final String DEFAULT_SERVERNAME = "jjhtest.hengdianworld.com";
    public static final int DEFAULT_PORT = 80;

    private String mHttp;
    private String mServerName;
    private int    mPort;

    public String getHttp() {
        return mHttp;
    }

    public String getServerName() {
        return mServerName;
    }

    public int getPort() {
        return mPort;
    }

    public void setHttp(String http) {
        mHttp = http;
    }

    public void setServerName(String serverName) {
        mServerName = serverName;
    }

    public void setPort(int port) {
        mPort = port;
    }
}

package com.kgyam.ratelimiter.config;

import java.util.concurrent.TimeUnit;

public class FixAlgConfig extends AlgConfig {
    /*
    时间间隔，单位毫秒
     */
    private long span;


    public FixAlgConfig() {
    }


    public long getSpan() {
        return span;
    }

    public void setSpan(long span) {
        this.span = span;
    }
}

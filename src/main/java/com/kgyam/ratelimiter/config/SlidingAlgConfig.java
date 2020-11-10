package com.kgyam.ratelimiter.config;

public class SlidingAlgConfig extends AlgConfig {

    private long span;

    public SlidingAlgConfig() {
    }

    public long getSpan() {
        return span;
    }

    public void setSpan(long span) {
        this.span = span;
    }
}

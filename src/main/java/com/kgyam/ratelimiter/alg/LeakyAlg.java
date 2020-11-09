package com.kgyam.ratelimiter.alg;

public class LeakyAlg implements LimterAlg {
    public boolean tryAcquire(String appid, String url) {
        return false;
    }
}

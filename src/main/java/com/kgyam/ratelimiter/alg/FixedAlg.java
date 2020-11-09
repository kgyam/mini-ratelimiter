package com.kgyam.ratelimiter.alg;

public class FixedAlg implements LimterAlg {
    public boolean tryAcquire(String appid, String url) {
        return false;
    }
}

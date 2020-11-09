package com.kgyam.ratelimiter.alg;

public interface LimterAlg {
    boolean tryAcquire(String appid, String url);
}

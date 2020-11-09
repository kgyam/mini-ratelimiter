package com.kgyam.ratelimiter.alg;

public class TokenAlg implements  LimterAlg {
    public boolean tryAcquire(String appid, String url) {
        return false;
    }
}

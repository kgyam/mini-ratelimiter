package com.kgyam.ratelimiter.alg;

public class LeakyAlg implements LimitAlg {
    public boolean tryAcquire() {
        return false;
    }
}

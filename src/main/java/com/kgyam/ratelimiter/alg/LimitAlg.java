package com.kgyam.ratelimiter.alg;

public interface LimitAlg {
    boolean tryAcquire();
}

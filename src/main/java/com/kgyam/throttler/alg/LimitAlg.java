package com.kgyam.throttler.alg;

public interface LimitAlg {
    boolean tryAcquire();
}

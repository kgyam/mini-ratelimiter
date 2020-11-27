package com.kgyam.throttler.enums;

public enum AlgType {
    FIXED("com.kgyam.throttler.alg.FixedAlg"),
    LEAKY("com.kgyam.throttler.alg.LeakyAlg"),
    SLIDING("com.kgyam.throttler.alg.SlidingAlg"),
    TOKEN_BUCKET("com.kgyam.throttler.alg.TokenBucketAlg");

    private String classpath;

    AlgType(String classpath) {
        this.classpath = classpath;
    }

    public String getClasspath() {
        return classpath;
    }
}

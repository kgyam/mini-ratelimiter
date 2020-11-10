package com.kgyam.ratelimiter.config;

public class TokenBucketAlgConfig extends AlgConfig {
    /*
    允许多少个令牌
     */
    private int permit;

    public TokenBucketAlgConfig() {
    }

    public int getPermit() {
        return permit;
    }

    public void setPermit(int permit) {
        this.permit = permit;
    }
}

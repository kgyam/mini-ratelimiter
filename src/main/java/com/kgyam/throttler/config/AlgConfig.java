package com.kgyam.throttler.config;

import com.kgyam.throttler.enums.AlgType;

public class AlgConfig {
    private AlgType algType;
    private int span;
    private int rate;
    /*
  允许多少个令牌 ,用于令牌桶,允许多少个
   */
    private int permit;

    public AlgConfig() {
    }

    public AlgType getAlgType() {
        return algType;
    }

    public int getSpan() {
        return span;
    }

    public int getRate() {
        return rate;
    }

    public int getPermit() {
        return permit;
    }
}

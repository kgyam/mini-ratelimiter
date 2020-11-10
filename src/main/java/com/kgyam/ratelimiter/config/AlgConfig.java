package com.kgyam.ratelimiter.config;

import com.kgyam.ratelimiter.enums.AlgType;

public class AlgConfig {
    String algType;

    public AlgConfig() {
    }

    public String getAlgType() {
        return algType;
    }

    public void setAlgType(String algType) {
        this.algType = algType;
    }
}

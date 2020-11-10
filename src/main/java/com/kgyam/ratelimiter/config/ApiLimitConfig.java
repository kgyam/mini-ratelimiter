package com.kgyam.ratelimiter.config;

/**
 * 每个APP下，对应的url的配置类
 */

public class ApiLimitConfig {
    /*
    url
     */

    private String url;
    /*
    对应得限流数
     */

    private int limit;


    private AlgConfig algConfig;


    public AlgConfig getAlgConfig() {
        return algConfig;
    }

    public void setAlgConfig(AlgConfig algConfig) {
        this.algConfig = algConfig;
    }

    public ApiLimitConfig() {
    }


    public String getUrl() {
        return url;
    }

    public int getLimit() {
        return limit;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

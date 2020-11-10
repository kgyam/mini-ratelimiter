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


    private String algType;


    public String getAlgType() {
        return algType;
    }

    public void setAlgType(String algType) {
        this.algType = algType;
    }

    public ApiLimitConfig() {
    }

    public ApiLimitConfig(String url, int limit, String algType) {
        this.url = url;
        if (limit <= 0) {
            this.limit = 0;
        } else {
            this.limit = limit;
        }
        this.algType = algType;
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

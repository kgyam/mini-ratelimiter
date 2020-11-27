package com.kgyam.throttler.config;

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

package com.kgyam.throttler.config.parser;

import com.kgyam.throttler.config.RuleConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ParserDelegate implements ConfigParser {

    private static ConfigType[] configTypes;
    private static final String FILE_NAME = "mini-ratelimiter";
    private static Map<ConfigType, ConfigParser> parserMap;

    static {
        configTypes = new ConfigType[]{ConfigType.Properties,
                ConfigType.Yml,
                ConfigType.Xml};
        parserMap = new HashMap();
        parserMap.put(ConfigType.Json, new JsonParser());
        parserMap.put(ConfigType.Yml, new YmlParser());
        parserMap.put(ConfigType.Xml, new XmlParser());
        parserMap.put(ConfigType.Properties, new PropertiesParser());
    }

    public RuleConfig load(InputStream in) {
        for (ConfigType type : configTypes) {
            String suffix = type.getSuffix();
            try (InputStream input = load(suffix)) {
                if (input != null) {
                    return parserMap.get(type).load(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private InputStream load(String suffix) {
        String file = "/" + FILE_NAME + suffix;
        return this.getClass().getResourceAsStream(file);
    }


    enum ConfigType {
        Yml(".yml"),
        Xml(".xml"),
        Json(".json"),
        Properties(".properties");

        private String suffix;

        ConfigType(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return suffix;
        }
    }
}

package com.github.dapeng.dms.util;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-02 11:45 AM
 */
public class SysEnvUtil {
    public static final String KEY_SOA_ZOOKEEPER_HOST = "soa.zookeeper.host";
    private static final String KEY_OPEN_AUTH_ENABLE = "soa.open.auth.enable";

    public static final String SOA_ZOOKEEPER_HOST = get(KEY_SOA_ZOOKEEPER_HOST, null);
    /**
     * 默认开启open接口鉴权
     */
    public static final String OPEN_AUTH_ENABLE = get(KEY_OPEN_AUTH_ENABLE, "true");


    private static String get(String key, String defaultValue) {
        String envValue = System.getenv(key.replaceAll("\\.", "_"));

        if (envValue == null) {
            return System.getProperty(key, defaultValue);
        }

        return envValue;
    }

}

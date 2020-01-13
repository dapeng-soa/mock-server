package com.github.dapeng.dms.web.util;


import org.asynchttpclient.*;

import java.nio.charset.StandardCharsets;

public class AHCExecutor {


    private static AsyncHttpClient asyncHttpClient;

    static {
        asyncHttpClient = Dsl.asyncHttpClient(
                Dsl.config()
                        .setKeepAlive(true)
                        .setConnectTimeout(1000)
                        .setRequestTimeout(10000));
    }


    public static String execute(String url) {
        try {
            return asyncHttpClient.executeRequest(Dsl.get(url)).get().getResponseBody(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "{}";
        }
    }

}

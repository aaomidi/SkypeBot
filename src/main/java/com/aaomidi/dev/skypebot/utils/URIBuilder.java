package com.aaomidi.dev.skypebot.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class URIBuilder {
    public static String buildURI(final String base, final Map<String, String> queryParams) {
        assert base != null;
        if (queryParams == null || queryParams.isEmpty()) {
            return base;
        }
        final StringBuilder sb = new StringBuilder(base.length() + queryParams.size() * 16);
        sb.append(base);
        boolean isFirst = true;
        for (Map.Entry<String, String> param : queryParams.entrySet()) {
            sb
                    .append(isFirst ? '?' : '&')
                    .append(encodeURIComponent(param.getKey()))
                    .append("=")
                    .append(encodeURIComponent(param.getValue()));
            isFirst = false;
        }
        return sb.toString();
    }

    private static String encodeURIComponent(final String component) {
        try {
            return URLEncoder.encode(component, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 died, sorry." + e);
        }
    }
}

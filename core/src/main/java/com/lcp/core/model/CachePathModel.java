package com.lcp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CachePathModel {
    private String cacheName;
    private String cacheKey;

    public String getFullPath() {
        return String.format("%s--%s", cacheName, cacheKey);
    }
}

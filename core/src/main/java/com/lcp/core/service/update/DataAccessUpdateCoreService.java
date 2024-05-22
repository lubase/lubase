package com.lcp.core.service.update;

import com.lcp.core.model.DbCollection;
import lombok.SneakyThrows;

/**
 * DataAccess 核心更新服务
 */
public interface DataAccessUpdateCoreService {
    /**
     * 数据更新
     *
     * @param collection
     * @return
     */
    @SneakyThrows
    Integer update(DbCollection collection);
}

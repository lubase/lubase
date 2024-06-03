package com.lubase.core.service.multiApplications.model;

import lombok.Data;

/**
 * <p>
 *   文件上传，请求参数
 * </p>
 * @author bluesky
 * @jdk   1.8
 */
@Data
public class FileResParamVO {
    /**
     * 文件Md5值
     */
    public String md5;
    /**
     * 记录id
     */
    public Long id;
    /**
     * 文件大小 k
     */
    public  double size;
}

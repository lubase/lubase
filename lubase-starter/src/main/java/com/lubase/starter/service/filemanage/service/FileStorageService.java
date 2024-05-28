package com.lubase.starter.service.filemanage.service;

import java.io.IOException;

/**
 * 文件存储服务
 */
public interface FileStorageService {
    /**
     * 将二进制文件流进行存储
     *
     * @param group      预留参数，暂时无用
     * @param fileName   文件原始名字
     * @param uniqueName 文件唯一名字，自动生成
     * @param bs         文件流
     * @return 文件存储路径
     * @throws IOException
     */
    String writeFile(String group, String fileName, String uniqueName, byte[] bs) throws IOException;

    /**
     * 根据文件存储路径和文件名字读取文件
     *
     * @param filePath   文件路径
     * @param fileName   文件原始名字
     * @param uniqueName 文件唯一名字，在存储文件时生成的名字
     * @return
     */
    byte[] readFile(String filePath, String fileName, String uniqueName);

    /**
     * 此方法已经废弃
     * @param filePath
     * @param fileName
     * @return
     */
    @Deprecated
    default Long exists(String filePath, String fileName) {
        return 0L;
    }
}

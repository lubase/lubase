package com.lubase.core.service.filemanage.service.impl;

import com.lubase.core.service.filemanage.service.FileStorageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service()
public class LocalFileStorageService implements FileStorageService {

    @Value("${custom.file-upload.rootPath:}")
    String rootPathStr;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String writeFile(String group, String fileName, String uniqueName, byte[] bs) throws IOException {
        String filePath = null;
        try {
            Path rootPath = getRootPath();
            Path groupPath = getGroupPath(group);
            Path fullPath = Path.of(rootPath.toString(), groupPath.toString());
            if (!Files.exists(fullPath)) {
                if (!(new File(fullPath.toString()).mkdirs())) {
                    throw new IOException("路径创建失败" + fullPath);
                }
            }
            String fullFileName = Path.of(rootPath.toString(), groupPath.toString(), uniqueName).toString();
            FileOutputStream fileOutputStream = new FileOutputStream(fullFileName, false);
            fileOutputStream.write(bs);
            fileOutputStream.close();

            filePath = groupPath.toString();
        } catch (Exception ex) {
            log.error("写入文件失败：" + fileName, ex);
        }
        return filePath;
    }

    private Path getRootPath() {
        Path rootPath;
        if (StringUtils.isEmpty(rootPathStr)) {
            rootPath = Path.of(System.getProperty("user.dir"), "upload");
        } else {
            rootPath = Path.of(rootPathStr);
        }
        return rootPath;
    }

    private Path getGroupPath(String group) {
        return Path.of(group, String.valueOf(LocalDateTime.now().toLocalDate().getYear()), LocalDateTime.now().toLocalDate().format(formatter));
    }

    @SneakyThrows
    @Override
    public byte[] readFile(String filePath, String fileName, String uniqueName) {
        Path rootPath = getRootPath();
        Path fullPath = null;
        if (filePath.startsWith(rootPath.toString())) {
            // 这是为了兼容以前的文件存储路径
            fullPath = Path.of(filePath, uniqueName);
        } else {
            fullPath = Path.of(rootPath.toString(), filePath, uniqueName);
        }

        File file = new File(fullPath.toString());
        if (!file.exists()) {
            return null;
        }
        if (file.exists() && file.length() > Integer.MAX_VALUE) {
            return null;
        }
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream bueerIS = null;
        try {
            bueerIS = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = bueerIS.read(buffer, 0, buf_size))) {
                byteOS.write(buffer, 0, len);
            }
            return byteOS.toByteArray();
        } catch (Exception ex) {
            throw ex;
        } finally {
            bueerIS.close();
            byteOS.close();
        }
    }

    @Override
    public Long exists(String filePath, String fileName) {
        try {
            Path rootPath = getRootPath();
            Path fullPath = null;
            if (filePath.startsWith(rootPath.toString())) {
                // 这是为了兼容以前的文件存储路径
                fullPath = Path.of(filePath, fileName);
            } else {
                fullPath = Path.of(rootPath.toString(), filePath, fileName);
            }

            File file = new File(fullPath.toString());
            if (file.exists() && file.isFile()) {
                return file.length();
            }

        } catch (Exception ex) {
            log.warn("检查文件路径错误：" + filePath + fileName, ex.getMessage());
        }
        return 0L;
    }
}

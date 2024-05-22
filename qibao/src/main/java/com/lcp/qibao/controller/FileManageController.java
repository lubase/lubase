package com.lcp.qibao.controller;

import com.lcp.core.exception.ParameterNotFoundException;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.qibao.auto.entity.SdFileInfoEntity;
import com.lcp.qibao.service.filemanage.model.FileInfoVO;
import com.lcp.qibao.service.filemanage.service.FileManageService;
import com.lcp.qibao.response.ResponseData;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileManageController {
    @Autowired
    FileManageService fileManageService;

    @SneakyThrows
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseData<Map> upload(@RequestParam @NonNull String dataId, @RequestParam String fileKey, @RequestParam MultipartFile file) {
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(fileKey)) {
            throw new ParameterNotFoundException("appId or dataId or fileKey not null");
        }
        if (!fileKey.contains(",")) {
            throw new ParameterNotFoundException("fileKey参数格式不正确，格式为 %s,%s ");
        }
        try {
            Long.parseLong(dataId);
        } catch (Exception ex) {
            throw new WarnCommonException("appId or dataId master by long");
        }
        if (file.getBytes().length == 0) {
            throw new WarnCommonException("不能上传空文件");
        }
        FileInfoVO vo = new FileInfoVO();
        vo.setFileKey(fileKey);
        vo.setDataId(dataId);
        vo.setOriginalFileName(file.getOriginalFilename());
        vo.setData(file.getBytes());
        // appId 暂时设置为0
        vo.setAppId(0L);

        String fileRelationId = fileManageService.upload(vo);

        Map<String, String> map = new HashMap<>();
        map.put("id", fileRelationId);
        return ResponseData.success(map);
    }

    @SneakyThrows
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void fileDownload(HttpServletResponse response, @RequestParam String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ParameterNotFoundException("fileInfoId not null");
        }
        SdFileInfoEntity fileInfo = fileManageService.getFileInfoByRelationId(id);
        if (fileInfo == null) {
            throw new WarnCommonException("文件不存在");
        }
        try {
            byte[] fileData = fileManageService.readFile(fileInfo);
            if (fileData == null || fileData.length == 0) {
                throw new WarnCommonException("文件不存在");
            }
            String fileType = "attachment";
            String type = new MimetypesFileTypeMap().getContentType(fileInfo.getEx_type());
            response.setHeader("Content-Type", type);
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(fileInfo.getOriginal_name(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", fileType + ";filename*=utf-8''" + fileName + "");
            response.getOutputStream().write(fileData);
        } catch (Exception ex) {
            log.error("文件下载失败", ex);
            throw new WarnCommonException("文件下载失败，请联系管理员");
        }
    }

    @SneakyThrows
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseData<Boolean> fileDelete(@RequestParam String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ParameterNotFoundException("id not null");
        }
        try {
            Long.parseLong(id);
        } catch (Exception ex) {
            throw new WarnCommonException("id master by long");
        }
        return ResponseData.success(fileManageService.deleteFileRelation(id));
    }

    /**
     * 文件复制
     *
     * @param fromDataId
     * @param fromColumnTag
     * @param toDataId
     * @param toColumnTag
     * @return
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public ResponseData<Integer> copy(@RequestParam @NonNull String fromDataId, @RequestParam @NonNull String fromColumnTag,
                                      @RequestParam @NonNull String toDataId, @RequestParam @NonNull String toColumnTag) {

        Integer result = fileManageService.copyFileRelation(fromDataId, fromColumnTag, toDataId, toColumnTag);
        return ResponseData.success(result);
    }
}

package com.lubase.starter.controller;

import com.google.common.collect.ImmutableList;
import com.lubase.core.exception.InvokeCommonException;
import com.lubase.starter.service.multiApplications.AppConfig;
import com.lubase.starter.service.multiApplications.IExtendAppService;
import com.lubase.starter.service.multiApplications.model.FileReqParamBO;
import com.lubase.starter.service.multiApplications.model.FileResParamVO;
import com.lubase.starter.response.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/loadJar")
public class LoadExtendAppController {
    @Autowired
    IExtendAppService appService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseData<FileResParamVO> fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws InvokeCommonException, IOException {
        byte[] data = file.getBytes();
        if (null == data || data.length == 0) {
            //文件不存在
            throw new InvokeCommonException("文件不存在");
        }
        //解析参数
        FileReqParamBO params = new FileReqParamBO();

        params.setFileKey("jar");
        params.setDataId(Long.valueOf(123445));
        params.setData(data);
        //获取workflow-0.0.1-SNAPSHOT
        String[] OriginalNameArray = file.getOriginalFilename().split(".jar");
        String verSion = "";
        String JarName = "";
        if (OriginalNameArray.length > 0) {
            //拆分成workflow    | 0.0.1  |  SNAPSHOT
            String[] JarNameArray = OriginalNameArray[0].split("-");
            JarName = JarNameArray[0];
            //拼装0.0.1-SNAPSHOT 预留最后一位单独组装
            for (int i = 1; i < JarNameArray.length - 1; i++) {
                verSion += JarNameArray[i] + "-";
            }
            verSion += JarNameArray[JarNameArray.length - 1];
        }
        params.setFileName(file.getOriginalFilename());
        try {
            AppConfig AppConfig = new AppConfig();
            AppConfig.setName(JarName);
            AppConfig.setEnabled(true);
            AppConfig.setVersion(verSion);
            URL url = new URL("file", "", -1, "");
            AppConfig.setModuleUrl(ImmutableList.of(url));
        } catch (MalformedURLException e) {

        }
        if (StringUtils.isEmpty(params.getFileKey().trim()) || StringUtils.isEmpty(params.getFileKey().trim())) {
            throw new InvokeCommonException("参数为空");
        }
        return null;
    }

    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    public String reload(@RequestParam String verSion, @RequestParam String jarName, @RequestParam String path) throws InvokeCommonException, IOException {
        String fileName = "D:\\code\\qibao-server\\upload\\file\\jar\\workflow-0.0.1-SNAPSHOT.jar";
        File fileJar = new File(fileName);
        if (!fileJar.exists()) {
            System.out.println("未找到文件");
        }
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("file:" + fileJar.getAbsolutePath()));

        AppConfig AppConfig = new AppConfig();
        AppConfig.setName(jarName);
        AppConfig.setEnabled(true);
        AppConfig.setVersion(verSion);
        List<String> list = new ArrayList<>();
        list.add("com.lcp.*");
        AppConfig.setOverridePackages(list);
        URL url = new URL("file", "", -1, path);
        AppConfig.setModuleUrl(urls);
        appService.loadAndRegister(AppConfig);
        return "success";
    }
}

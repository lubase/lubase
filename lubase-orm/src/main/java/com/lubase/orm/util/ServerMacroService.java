package com.lubase.orm.util;

import com.lubase.orm.service.AppHolderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ServerMacroService {

    @Autowired
    AppHolderService appHolderService;

    public static final String serverMacroPre = "@@S.";

    java.time.format.DateTimeFormatter formatterDate = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
    java.time.format.DateTimeFormatter formatterTime = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
    java.time.format.DateTimeFormatter formatterDatetime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 如果是empty 则需要在解析sql前替换，过早替换会导致此条件不解析
     *
     * @param macroKey
     * @return
     */
    public Boolean isEmptyKey(String macroKey) {
        return macroKey.equals("@@S.empty");
    }

    @SneakyThrows
    public String getServerMacroByKey(String macroKey) {
        if (!macroKey.startsWith(serverMacroPre)) {
            return macroKey;
        }
        String val = "";
        if (macroKey.equals("@@S.date")) {
            val = LocalDateTime.now().format(formatterDate);
        } else if (macroKey.equals("@@S.time")) {
            val = LocalDateTime.now().format(formatterTime);
        } else if (macroKey.equals("@@S.datetime")) {
            val = LocalDateTime.now().format(formatterDatetime);
        } else if (macroKey.equals("@@S.userId")) {
            val = appHolderService.getUser().getId().toString();
        } else if (macroKey.equals("@@S.userCode")) {
            val = appHolderService.getUser().getCode();
        } else if (macroKey.equals("@@S.userName")) {
            val = appHolderService.getUser().getName();
        } else  if(macroKey.equals("@@S.userOrgId")){
            val = appHolderService.getUser().getOrgId();
        }
        else if (macroKey.equals("@@S.empty")) {
            val = "";
        } else {
            log.warn("服务端宏变量未实现" + macroKey);
            return null;
        }
        return val;
    }
}

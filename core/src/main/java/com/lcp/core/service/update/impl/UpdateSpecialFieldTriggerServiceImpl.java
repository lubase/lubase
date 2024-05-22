package com.lcp.core.service.update.impl;

import com.lcp.core.model.DbCollection;
import com.lcp.core.model.LoginUser;
import com.lcp.core.service.AppHolderService;
import com.lcp.core.service.IDGenerator;
import com.lcp.core.service.update.UpdateTriggerService;
import com.lcp.core.util.ServerMacroService;
import com.lcp.coremodel.*;
import com.lcp.coremodel.constrant.MacroCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * update 前处理特殊字段的值。例如：id等
 */
@Order(1)
@Component
@Slf4j
public class UpdateSpecialFieldTriggerServiceImpl implements UpdateTriggerService {
    @Autowired
    IDGenerator idGenerator;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    ServerMacroService serverMacroService;

    @Override
    public void beforeUpdate(DbCollection collection) {
// 特殊字段的赋值逻辑。id、create_by、create_time、update_by、update_time 等
        LoginUser user = appHolderService.getUser();
        //初始化时候默认是管理员用户
        if (user == null) {
            user = new LoginUser();
            user.setId(Long.parseLong("677413984038555649"));
        }
        Long[] ids = null;
        int idIndex = 0;
        for (DbEntity entity : collection.getData()) {
            if (entity.getDataState().equals(EDBEntityState.Added)) {
                if (StringUtils.isEmpty(entity.getId())) {
                    if (ids == null) {
                        //TODO: 此处应该用批量创建ID的方法来替代，避免id重复的问题
                        ids = new Long[collection.getData().size()];
                        for (int i = 0; i < collection.getData().size(); i++) {
                            ids[i] = idGenerator.nextId();
                        }
                    }
                    entity.setId(ids[idIndex++]);
                }
                entity.put("create_by", user.getId());
                entity.put("create_time", LocalDateTime.now());
                processDefaultValue(collection.getTableInfo(), entity, collection.isServer());
            } else if (entity.getDataState().equals(EDBEntityState.Modified)) {
                entity.putWithNoTrace("update_by", user.getId());
                entity.putWithNoTrace("update_time", LocalDateTime.now());
            }
        }
    }

    void processDefaultValue(DbTable tableInfo, DbEntity entity, Boolean isServer) {
        for (DbField field : tableInfo.getFieldList()) {
            // 只读和不可见字段会根据col_default  的设置进行默认值的处理
            if (StringUtils.isEmpty(field.getColDefault()) || field.getVisible() > EAccessGrade.Read.getIndex()) {
                continue;
            }
            //客户端更新模式处理只读字段和不可见字段默认值
            String colDefault = field.getColDefault();
            if (!isServer) {
                if (colDefault.indexOf(",") > 0 || colDefault.startsWith(MacroCost.clientMacroPre)) {
                    log.warn(String.format("字段 %s的默认值配置为：%s，字段属性不可见或者只读，默认值配置无效", field.getId(), colDefault));
                    entity.remove(field.getCode());
                } else if (colDefault.startsWith(MacroCost.serverMacroPre)) {
                    //替换宏变量
                    entity.put(field.getCode(), serverMacroService.getServerMacroByKey(colDefault));
                } else {
                    entity.put(field.getCode(), colDefault);
                }
                //处理默认值设置为可编辑状态，否则无权限进行编辑
                field.setVisible(4);
            } else {
                //服务端模式如何data对象没有字段的值，则添加默认值。如果有则表明已经处理不此处不再处理
                if (!entity.containsKey(field.getCode())) {
                    if (colDefault.startsWith(MacroCost.serverMacroPre)) {
                        //替换宏变量
                        entity.put(field.getCode(), serverMacroService.getServerMacroByKey(colDefault));
                    } else if (!colDefault.startsWith(MacroCost.clientMacroPre)) {
                        entity.put(field.getCode(), colDefault);
                    }
                }
            }
        }
    }
}

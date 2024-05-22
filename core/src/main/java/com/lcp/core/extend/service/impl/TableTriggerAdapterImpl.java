package com.lcp.core.extend.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.extend.ExtendAppLoadCompleteService;
import com.lcp.core.extend.ITableTrigger;
import com.lcp.core.extend.service.TableTriggerAdapter;
import com.lcp.coremodel.DbTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TableTriggerAdapterImpl implements TableTriggerAdapter, ExtendAppLoadCompleteService {
    List<ITableTrigger> tableTriggerList;

    @Override
    public void clearData() {
        tableTriggerList = null;
    }

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (tableTriggerList == null) {
            tableTriggerList = new ArrayList<>();
            Map<String, ITableTrigger> triggerMap = applicationContext.getBeansOfType(ITableTrigger.class);
            for (String key : triggerMap.keySet()) {
                tableTriggerList.add(triggerMap.get(key));
            }
        }
        log.info(String.format("ITableTrigger触发器列表总数量为：%s，明细为：%s", tableTriggerList.size(), JSON.toJSONString(tableTriggerList)));
    }

    @Override
    public List<ITableTrigger> getTableTriggerList(DbTable tableInfo) {
        String tableCode = tableInfo.getCode();
        if (tableTriggerList == null) {
            return new ArrayList<>();
        }
        //专属触发器
        List<ITableTrigger> list = tableTriggerList
                .stream().filter(t -> t.getTriggerTableCode().equals(tableCode)).collect(Collectors.toList());

        //全局触发器
        for (ITableTrigger trigger : tableTriggerList) {
            if (trigger.getTriggerTableCode().equals("*")) {
                if (trigger.GlobalTriggerFilter(tableInfo)) {
                    list.add(trigger);
                }
            }
        }

        return list;
    }

    @Override
    public List<ITableTrigger> getTableTriggerList() {
        if (tableTriggerList == null) {
            return new ArrayList<>();
        }
        return tableTriggerList;
    }

}

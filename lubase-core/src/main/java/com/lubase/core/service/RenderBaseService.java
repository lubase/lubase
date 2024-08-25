package com.lubase.core.service;

import com.lubase.core.util.ClientMacro;
import com.lubase.orm.TableFilter;
import com.lubase.orm.util.ServerMacroService;
import org.springframework.util.StringUtils;

public interface RenderBaseService {

    ServerMacroService getServerMacroService();

    /**
     * 宏变量替换
     *
     * @param tableFilter 标准的TableFilter
     * @param clientMacro 客户端宏变量
     */
    default void replaceClientMacro(TableFilter tableFilter, ClientMacro clientMacro) {
        if (tableFilter.getChildFilters() != null && tableFilter.getChildFilters().size() == 1 && tableFilter.getChildFilters().get(0) == null) {
            tableFilter.setChildFilters(null);
        }
        if (tableFilter.getChildFilters() != null) {
            for (TableFilter filter : tableFilter.getChildFilters()) {
                replaceClientMacro(filter, clientMacro);
            }
        } else {
            Object objectVal = tableFilter.getFilterValue();
            if (objectVal == null || StringUtils.isEmpty(objectVal)) {
                return;
            }
            String strVal = objectVal.toString();
            // 必须客户端宏变量不为空才可以替换，否则无值此条件会作废
            if (strVal.startsWith(ClientMacro.clientMacroPre)) {
                //替换宏变量
                if (clientMacro.containsKey(strVal)
                        && !StringUtils.isEmpty(clientMacro.get(strVal))) {
                    tableFilter.setFilterValue(clientMacro.get(strVal));
                } else {
                    //设置一个不存在的条件
                    tableFilter.setFilterName("id");
                    tableFilter.setFilterValue(0);
                }
            } else if (strVal.startsWith(ClientMacro.serverMacroPre) && !getServerMacroService().isEmptyKey(strVal)) {
                tableFilter.setFilterValue(getServerMacroService().getServerMacroByKey(strVal));
            }
        }
    }

}

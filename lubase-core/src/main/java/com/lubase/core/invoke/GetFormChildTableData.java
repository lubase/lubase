package com.lubase.core.invoke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.model.SearchCondition;
import com.lubase.core.model.SearchVO;
import com.lubase.core.model.customForm.ChildTableDataVO;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.core.util.ClientMacro;
import com.lubase.core.util.SearchCondition2TableFilterService;
import com.lubase.model.DbTable;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.constant.CommonConst;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.ServerMacroService;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.extend.LoadSubTableDataService;
import com.lubase.core.extend.service.CustomFormServiceAdapter;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.entity.DmTableRelationEntity;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 获取表单子表数据
 */
public class GetFormChildTableData implements IInvokeMethod {
    @Autowired
    DataAccess dataAccess;

    @Autowired
    CustomFormServiceAdapter customFormServiceAdapter;

    @Autowired
    SearchCondition2TableFilterService searchCondition2TableFilterService;

    @Override
    public String getDescription() {
        return "获取表单子表的数据";
    }

    @Override
    public boolean checkRight() {
        return false;
    }

    @Autowired
    ServerMacroService serverMacroService;

    @Autowired
    CustomFormDataService customFormDataService;

    @Override
    public String getId() {
        return "688087055355351040";
    }

    /**
     * 此方法暂时不考虑子表数据翻页，如果翻页需要再传递queryParam 参数
     *
     * @param mapParam key="dataId",value="字段值"
     *                 key="serialNum",value="随机唯一值"
     *                 key="formId",value="formId"
     * @return
     */
    @Override
    @SneakyThrows
    public Object exe(HashMap<String, String> mapParam) {
        // searchParam
        Long dataId = Long.parseLong(checkAndGetParam("dataId", mapParam));
        String serialNum = checkAndGetParam("serialNum", mapParam);
        Long formId = Long.parseLong(checkAndGetParam("formId", mapParam));
        Integer pageSize = 0, pageIndex = 0;
        if (mapParam.containsKey("pageIndex") && !StringUtils.isEmpty(mapParam.get("pageIndex"))) {
            pageIndex = Math.max(1, Integer.parseInt(mapParam.get("pageIndex")));
        }
        if (mapParam.containsKey("pageSize") && !StringUtils.isEmpty(mapParam.get("pageSize"))) {
            pageSize = Math.max(1, Integer.parseInt(mapParam.get("pageSize")));
        }
        String searchParamStr = "";
        if (mapParam.containsKey("searchParam")) {
            searchParamStr = mapParam.get("searchParam");
        }
        //1、获取表单子表查询对象
        DmCustomFormEntity formEntity = customFormDataService.selectById(formId.toString());
        if (formEntity == null) {
            throw new InvokeCommonException("formId 参数异常");
        }
        Long mainTableId = formEntity.getTable_id();

        //2、获取表单主表与子表关联关系
        ChildTableSetting childTable = customFormDataService.getChildTableFromSettingStr(formEntity.getChild_table(), serialNum);
        if (StringUtils.isBlank(formEntity.getChild_table()) || childTable == null || childTable.getQueryOption() == null) {
            throw new InvokeCommonException("表单未设置子表配置信息，请检查");
        }
        QueryOption childQuery = childTable.getQueryOption();
        //如果设置了查询id和查询类型则调用扩展服务查询
        if (childQuery.getServerId() != null && childQuery.getServerId() > 0L && childQuery.getQueryType() == 4) {
            LoadSubTableDataService service = customFormServiceAdapter.getSubTableDataServiceById(mainTableId.toString(), childQuery.getServerId().toString());
            if (service != null) {
                return service.loadChildTableData(dataId.toString());
            } else {
                throw new WarnCommonException(String.format("子表服务%s%s未实现", service.getId(), service.getDescription()));
            }
        } else {
            Long childTableId = childTable.getQueryOption().getTableId();
            DbTable childTableDbTable = dataAccess.initTableInfoByTableId(mainTableId);
            if (childTableDbTable == null) {
                throw new WarnCommonException("子表" + childTable.getTitle() + childTable.getQueryOption().getTableName() + "已经删除请调整表单");
            }
            if (mainTableId == null || childTableId == null) {
                throw new WarnCommonException("表单主表信息、从表信息设置异常，表id为空");
            }
            //查询主从表对应关系
            DbEntity tableRelation = getTableRelation(mainTableId, childTableId);
            if (tableRelation == null) {
                throw new WarnCommonException("主从表关系设置不正确，请检查");
            }
            //3、查询子表数据
            TableFilterWrapper filterWrapper = TableFilterWrapper.and();
            filterWrapper.eq(tableRelation.get("fk_column_code").toString(), dataId);
            if (childTable.getQueryOption().getTableFilter() != null && !StringUtils.isEmpty(childTable.getQueryOption().getTableFilter().getFilterName())) {
                filterWrapper.addFilter(childTable.getQueryOption().getTableFilter());
            }
            QueryOption queryOption = childTable.getQueryOption();
            if (StringUtils.isEmpty(queryOption.getFixField())) {
                queryOption.setFixField("*");
            }
            if (!org.springframework.util.StringUtils.isEmpty(searchParamStr)) {
                List<SearchCondition> list = JSON.parseArray(searchParamStr, SearchCondition.class);
                TableFilter searchFilter = searchCondition2TableFilterService.convertToTableFilter(list);
                if (searchFilter != null) {
                    filterWrapper.addFilter(searchFilter);
                }
            }
            queryOption.setTableFilter(filterWrapper.build());

            if (pageIndex > 0) {
                queryOption.setPageIndex(pageIndex);
            }
            if (pageSize > 0) {
                queryOption.setPageSize(pageSize);
            }
            DbCollection collChild = dataAccess.query(queryOption);
            //如果指定了查询顺序则调整列顺序
            if (!StringUtils.isEmpty(queryOption.getFixField()) && !queryOption.getFixField().equals("*")) {
                int i = 1;
                for (DbField f : collChild.getTableInfo().getFieldList()) {
                    f.setOrderId(i++);
                }
            }
            ChildTableDataVO childTableDataVO = new ChildTableDataVO();
            childTableDataVO.setDbCollection(collChild);
            childTableDataVO.setSearch(getSearchObject(childTable.getSearchInfo(), collChild.getTableInfo()));
            return childTableDataVO;
        }
    }

    private SearchVO getSearchObject(String searchStr, DbTable table) {
        SearchVO searchVO = new SearchVO();
        List<SearchCondition> list = JSON.parseArray(searchStr, SearchCondition.class);
        if (list == null || list.size() == 0) {
            return searchVO;
        }
        HashMap<String, DbField> fieldBOHashMap = new HashMap<>();
        for (SearchCondition condition : list) {
            //如果defaultValue属性不为空，则给此属性用服务端宏变量重新赋值
            if (condition.getDefaultValue() != null && condition.getDefaultValue().startsWith(ClientMacro.serverMacroPre)) {
                condition.setDefaultValue(serverMacroService.getServerMacroByKey(condition.getDefaultValue()));
            }
            if (condition.getDefaultValueName() != null && condition.getDefaultValueName().startsWith(ClientMacro.serverMacroPre)) {
                condition.setDefaultValueName(serverMacroService.getServerMacroByKey(condition.getDefaultValueName()));
            }
            DbField field = table.getFieldList().stream().filter(f -> f.getCode().equals(condition.getColumnCode())).findFirst().orElse(null);
            fieldBOHashMap.put(field.getCode(), field);
        }
        searchVO.setFieldInfo(fieldBOHashMap);
        searchVO.setFilter(list);
        return searchVO;
    }

    private DmTableRelationEntity getTableRelation(Long mainTableId, Long childTableId) {
        DmTableRelationEntity entity = null;
        QueryOption queryOption = new QueryOption("dm_table_relation");
        TableFilterWrapper filterWrapper = TableFilterWrapper.and().eq("main_table_id", mainTableId).eq("child_table_id", childTableId);
        queryOption.setTableFilter(filterWrapper.build());
        List<DmTableRelationEntity> relationEntityList = dataAccess.query(queryOption).getGenericData(DmTableRelationEntity.class);
        if (relationEntityList.size() == 1) {
            entity = relationEntityList.get(0);
        }
        return entity;
    }
}

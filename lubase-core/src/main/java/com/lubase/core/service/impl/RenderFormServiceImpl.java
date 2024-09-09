package com.lubase.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.entity.DmFormFilterEntity;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.extend.IFormTrigger;
import com.lubase.core.extend.LoadSubTableDataService;
import com.lubase.core.extend.LookupColumnDataService;
import com.lubase.core.extend.service.CustomFormServiceAdapter;
import com.lubase.core.model.*;
import com.lubase.core.model.customForm.ChildTableDataVO;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.model.customForm.ColumnLookupInfoVO;
import com.lubase.core.model.customForm.ColumnLookupParamModel;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.core.service.FormRuleService;
import com.lubase.core.service.RenderFormService;
import com.lubase.core.util.ClientMacro;
import com.lubase.core.util.SearchCondition2TableFilterService;
import com.lubase.model.*;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.extend.service.ColumnRemoteServiceAdapter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.EColumnType;
import com.lubase.orm.model.LookupMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.IDGenerator;
import com.lubase.orm.util.DbEntityTool;
import com.lubase.orm.util.ServerMacroService;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 获取自定义表单的配置和数据。
 * </p>
 *
 * @author bluesky
 */
@Slf4j
@Service
public class RenderFormServiceImpl implements RenderFormService {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    IDGenerator idGenerator;
    @Autowired
    FormRuleService formRuleService;
    @Autowired
    ServerMacroService serverMacroService;
    @Autowired
    CustomFormServiceAdapter customFormServiceAdapter;
    @Autowired
    CustomFormDataService customFormDataService;
    @Autowired
    ColumnRemoteServiceAdapter columnRemoteServiceAdapter;

    @Autowired
    SearchCondition2TableFilterService searchCondition2TableFilterService;

    @SneakyThrows
    @Override
    public ColumnLookupInfoVO getColLookupData(ColumnLookupParamModel columnLookupParam) {
        if (StringUtils.isEmpty(columnLookupParam.getFormId()) || StringUtils.isEmpty(columnLookupParam.getFuncCode())
                || StringUtils.isEmpty(columnLookupParam.getColumnId()) || StringUtils.isEmpty(columnLookupParam.getClientMacro())) {
            throw new ParameterNotFoundException("formId,funcCode,columnId,clientMacro");
        }
        ColumnLookupInfoVO columnLookupInfoVO = new ColumnLookupInfoVO();
        Long columnId = 0L;
        try {
            columnId = Long.parseLong(columnLookupParam.getColumnId());
        } catch (Exception ex) {
            log.error("column id not found {}", columnLookupParam.getColumnId(), ex);
            return columnLookupInfoVO;
        }
        DbField field = dataAccess.getDbFieldByColumnId(columnId);
        LookupMode lookupMode = LookupMode.FromJsonStr(field.getLookup());
        if (field == null || !field.getEleType().toString().equals("7") || lookupMode == null) {
            throw new InvokeCommonException(String.format("列 %s 关联信息不正确", columnLookupParam.getColumnId()));
        }
        if (StringUtils.isEmpty(lookupMode.getTableKey()) || StringUtils.isEmpty(lookupMode.getDisplayCol()) || StringUtils.isEmpty(lookupMode.getTableCode())) {
            throw new InvokeCommonException(String.format("列 %s 关联信息不正确", columnLookupParam.getColumnId()));
        }
        //优先获取service实现，其次获取grid_query配置实现，最后获取dm_column默认配置

        QueryOption clientQuery = null;
        try {
            if (!StringUtils.isEmpty(columnLookupParam.getQueryParam()) && !columnLookupParam.getQueryParam().equals("{}")) {
                clientQuery = JSON.parseObject(columnLookupParam.getQueryParam(), QueryOption.class);
            }
        } catch (Exception ex) {
            log.error("客户端参数错误：" + columnLookupParam.getQueryParam());
        }
        LookupColumnDataService dataService = customFormServiceAdapter.getLookupColumnDataService(columnId.toString());
        if (StringUtils.isEmpty(lookupMode.getExtendCol())) {
            columnLookupInfoVO.setSearchCols(lookupMode.getDisplayCol());
        } else {
            columnLookupInfoVO.setSearchCols(String.format("%s,%s", lookupMode.getDisplayCol(), lookupMode.getExtendCol()));
        }

        if (dataService != null) {
            ClientMacro clientMacro = ClientMacro.init(columnLookupParam.getClientMacro());
            columnLookupInfoVO.setDbCollection(dataService.getColLookupData(clientMacro, columnLookupParam.getFormData(), clientQuery));
            columnLookupInfoVO.setTableKey(dataService.getTableKey());
            columnLookupInfoVO.setDisplayCol(dataService.getDisplayCol());
        } else {
            DmFormFilterEntity filterEntity = formRuleService.getFormFieldFilter(columnLookupParam.getFormId(), columnLookupParam.getColumnId());
            columnLookupInfoVO = getLookupInfoVO(columnLookupParam, lookupMode, filterEntity, clientQuery);
            if (filterEntity != null) {
                columnLookupInfoVO.setSearchCols(TypeConverterUtils.object2String(filterEntity.getSearch_filter(), ""));
                columnLookupInfoVO.setTableInfo(TypeConverterUtils.object2String(filterEntity.getGrid_info(), ""));
                columnLookupInfoVO.setGridInfo(TypeConverterUtils.object2String(filterEntity.getGrid_info(), ""));
            }
        }
        processSearchFilter(columnLookupInfoVO);
        return columnLookupInfoVO;
    }

    @SneakyThrows
    private ColumnLookupInfoVO getLookupInfoVO(ColumnLookupParamModel columnLookupParam, LookupMode lookupMode, DmFormFilterEntity serverFilterEntity, QueryOption clientQuery) {
        ColumnLookupInfoVO columnLookupInfoVO = new ColumnLookupInfoVO();

        QueryOption serverQuery = new QueryOption(lookupMode.getTableCode());

        if (serverQuery.getQueryType() != 1 || StringUtils.isEmpty(serverQuery.getTableName())) {
            throw new WarnCommonException(String.format("列 %s QueryType 必须为1，且tableName不能为空", columnLookupParam.getColumnId()));
        }
        serverQuery.setFixField("");//dbField 不再启用查询字段功能，应该form_filter获取，或者获取默认展示字段
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        TableFilter serverFilter = null;
        if (serverFilterEntity != null && !StringUtils.isEmpty(serverFilterEntity.getGrid_query())) {
            QueryOption settingQuery = JSON.parseObject(serverFilterEntity.getGrid_query(), QueryOption.class);
            if (settingQuery == null || settingQuery.getTableName() == null || !settingQuery.getTableName().equals(serverQuery.getTableName())) {
                throw new WarnCommonException(String.format("formFilter配置grid_query 与 字段配置不匹配，请联系系统管理员"));
            }
            serverQuery = settingQuery;
            //替换表单值
            JSONObject mapFormData = new JSONObject();
            if (!StringUtils.isEmpty(columnLookupParam.getFormData())) {
                mapFormData = (JSONObject) JSON.parse(columnLookupParam.getFormData());
            }
            //替换客户端宏变量
            ClientMacro clientMacro = ClientMacro.init(columnLookupParam.getClientMacro());
            for (String key : clientMacro.keySet()) {
                mapFormData.put(key, clientMacro.get(key));
            }
            serverFilter = serverQuery.getTableFilter();
            if (serverFilter != null) {
                replaceClientFormData(serverFilter, mapFormData);
            }
        }
        //处理默认查询字段
        if (StringUtils.isEmpty(serverQuery.getFixField())) {
            List<String> selectField = new ArrayList<>();
            selectField.add(lookupMode.getTableKey());
            selectField.add(lookupMode.getDisplayCol());
            if (!StringUtils.isEmpty(lookupMode.getExtendCol())) {
                selectField.add(lookupMode.getExtendCol());
            }
            if (!StringUtils.isEmpty(lookupMode.getSelectCols())) {
                for (String field : lookupMode.getSelectCols().split(",")) {
                    if (!selectField.contains(field)) {
                        selectField.add(field);
                    }
                }
            }
            serverQuery.setFixField(String.join(",", selectField));
        }
        //处理服务端查询条件
        if (DbEntityTool.tableFilterIsNotNull(serverFilter)) {
            filterWrapper.addFilter(serverFilter);
        }
        //处理客户端的搜索条件
        if (clientQuery != null) {
            serverQuery.setPageIndex(clientQuery.getPageIndex());
            serverQuery.setPageSize(clientQuery.getPageSize());
            // : 增加排序条件 2024/3/25
            if (!StringUtils.isEmpty(clientQuery.getSortField())) {
                serverQuery.setSortField(clientQuery.getSortField());
            } else {
                String sortField = String.format("%s.id desc", serverQuery.getTableName());
                serverQuery.setSortField(sortField);
            }
        }
        if (!StringUtils.isEmpty(columnLookupParam.getSearchParam())) {
            List<SearchCondition> list = JSON.parseArray(columnLookupParam.getSearchParam(), SearchCondition.class);
            TableFilter searchFilter = searchCondition2TableFilterService.convertToTableFilter(list);
            if (searchFilter != null) {
                filterWrapper.addFilter(searchFilter);
            }
        }
        serverQuery.setTableFilter(filterWrapper.build());
        //TODO 弹框列表获取数据 临时修改.非弹框列表 值为“{}”
        if (columnLookupParam.getQueryParam() != null && columnLookupParam.getQueryParam().length() > 5) {
            columnLookupInfoVO.setDbCollection(dataAccess.query(serverQuery));
        } else {
            columnLookupInfoVO.setDbCollection(dataAccess.queryAllData(serverQuery));
        }
        columnLookupInfoVO.setTableKey(lookupMode.getTableKey());
        columnLookupInfoVO.setDisplayCol(lookupMode.getDisplayCol());
        // 如果关联显示列是关联数据表或者关联服务列则把显示值替换为关联值
        String refDisplayCol = lookupMode.getDisplayCol() + "NAME";
        for (DbEntity entity : columnLookupInfoVO.getDbCollection().getData()) {
            if (entity.getRefData() != null && entity.getRefData().containsKey(refDisplayCol)) {
                entity.put(lookupMode.getDisplayCol(), entity.getRefData().get(refDisplayCol));
            }
        }
        return columnLookupInfoVO;
    }

    @SneakyThrows
    @Override
    public ColumnLookupInfoVO getColServiceData(ColumnLookupParamModel columnServiceParam) {
        if (StringUtils.isEmpty(columnServiceParam.getFormId()) || StringUtils.isEmpty(columnServiceParam.getFuncCode()) || StringUtils.isEmpty(columnServiceParam.getColumnId())) {
            throw new ParameterNotFoundException("formId,funcCode,columnId");
        }
        ColumnLookupInfoVO columnLookupInfoVO = new ColumnLookupInfoVO();
        Long columnId = 0L;
        try {
            columnId = Long.parseLong(columnServiceParam.getColumnId());
        } catch (Exception ex) {
            log.error("column id not found {}", columnServiceParam.getColumnId(), ex);
            return columnLookupInfoVO;
        }
        DbField field = dataAccess.getDbFieldByColumnId(columnId);
        if (field == null || !field.getEleType().toString().equals("10") || StringUtils.isEmpty(field.getServiceName())) {
            throw new InvokeCommonException(String.format("列 %s 关联信息不正确", columnServiceParam.getColumnId()));
        }
        QueryOption clientQuery = null;
        try {
            if (!StringUtils.isEmpty(columnServiceParam.getQueryParam()) && !columnServiceParam.getQueryParam().equals("{}")) {
                clientQuery = JSON.parseObject(columnServiceParam.getQueryParam(), QueryOption.class);
            }
        } catch (Exception exception) {
            // 不用记录日志，按全量数据返回即可
        }
        IColumnRemoteService service = columnRemoteServiceAdapter.getServiceByName(field.getServiceName());
        if (service == null) {
            throw new WarnCommonException("未找到服务列" + field.getServiceName());
        }
        if (columnServiceParam.getOnlyConfig() == null || columnServiceParam.getOnlyConfig() == 0) {
            columnLookupInfoVO.setDbCollection(service.getDataByFilter(clientQuery, columnServiceParam.getClientMacro()));
            columnLookupInfoVO.setSearchCols(service.searchCols());
        }
        columnLookupInfoVO.setTableKey(service.tableKey());
        columnLookupInfoVO.setDisplayCol(service.displayCol());
        processSearchFilter(columnLookupInfoVO);
        return columnLookupInfoVO;
    }

    private void processSearchFilter(ColumnLookupInfoVO columnLookupInfoVO) {
        List<SearchCondition> filterList = new ArrayList<>();
        if (!StringUtils.isEmpty(columnLookupInfoVO.getSearchCols())) {
            String[] searchCols = columnLookupInfoVO.getSearchCols().split(",");
            DbTable table = columnLookupInfoVO.getDbCollection().getTableInfo();
            for (String searchCol : searchCols) {
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setColumnCode(searchCol);
                //如果字段的eleType为1则模糊匹配
                DbField field = table.getFieldList().stream().filter(f -> f.getCode().equals(searchCol)).findFirst().orElse(null);
                if (field == null) {
                    continue;
                }
                if (field.getEleType().equals(EColumnType.Text.getStringValue())) {
                    searchCondition.setFilterType(ESearchConditionType.LikeAll.getType());
                } else {
                    searchCondition.setFilterType(ESearchConditionType.Equal.getType());
                }
                filterList.add(searchCondition);
            }
        }
        columnLookupInfoVO.setFilter(filterList);
    }

    @SneakyThrows
    @Override
    public ColumnRefPageVO getColRefPageInfo(String columnId) {
        if (StringUtils.isEmpty(columnId)) {
            return null;
        }
        DbField field = dataAccess.getDbFieldByColumnId(Long.parseLong(columnId));
        LookupMode lookup = LookupMode.FromJsonStr(field.getLookup());
        if (lookup == null || StringUtils.isEmpty(lookup.getPageId())) {
            return null;
        }
        DbEntity pageEntity = getPageInfo(lookup.getPageId());
        if (pageEntity == null) {
            throw new WarnCommonException("字段配置的关联页面信息不正确，请重新配置");
        }
        ColumnRefPageVO refPageVO = new ColumnRefPageVO();
        refPageVO.setPageId(lookup.getPageId());
        refPageVO.setDisplayCol(lookup.getDisplayCol());
        refPageVO.setTableKey(lookup.getTableKey());
        return refPageVO;
    }

    private DbEntity getPageInfo(String pageId) {
        DbCollection coll = dataAccess.queryById("ss_page", Long.parseLong(pageId), "id,page_name");
        if (coll.getData().size() > 0) {
            return coll.getData().get(0);
        } else {
            return null;
        }
    }

    @Override
    public Boolean checkFieldUniqueValue(String columnId, String val, String currentDataId) {
        DbField field = dataAccess.getDbFieldByColumnId(Long.parseLong(columnId));
        if (field == null || StringUtils.isEmpty(val) || StringUtils.isEmpty(currentDataId)) {
            return false;
        }
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        DbTable table = dataAccess.initTableInfoByTableCode(field.getTableCode());
        String config = table.getCustomConfig();
        if (config != null && !StringUtils.isEmpty(config)) {
            //第二位代表逻辑删除
            if (config.length() > 1 && config.toCharArray()[1] == '1') {
                TableFilterWrapper filterWrapper1 = TableFilterWrapper.or();
                filterWrapper1.eq("delete_tag", 0).isNull("delete_tag");
                filterWrapper.addFilter(filterWrapper1.build());
            }
        }
        QueryOption queryOption = new QueryOption(field.getTableCode());
        queryOption.setFixField("id");
        filterWrapper.eq(field.getCode(), val).ne("id", currentDataId);
        queryOption.setTableFilter(filterWrapper.build());
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection.getData().size() == 0;
    }


    void replaceClientFormData(TableFilter tableFilter, JSONObject mapFormData) {
        if (tableFilter.getChildFilters() != null) {
            for (TableFilter filter : tableFilter.getChildFilters()) {
                replaceClientFormData(filter, mapFormData);
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
                if (mapFormData.containsKey(strVal) && !StringUtils.isEmpty(mapFormData.get(strVal))) {
                    tableFilter.setFilterValue(mapFormData.get(strVal));
                } else {
                    log.warn("未能解析clientMacro %s", strVal);
                    //设置一个不存在的条件
                    tableFilter.setFilterName("id");
                    tableFilter.setFilterValue(0);
                }
            } else if (tableFilter.getValueType() == 3) {
                if (mapFormData.containsKey(strVal) && !StringUtils.isEmpty(mapFormData.get(strVal))) {
                    tableFilter.setFilterValue(mapFormData.get(strVal));
                } else {
                    log.warn("未能解析clientMacro %s", strVal);
                    //设置一个不存在的条件
                    tableFilter.setFilterName("id");
                    tableFilter.setFilterValue(0);
                }
            }
        }
    }

    @Override
    public CustomFormVO getAddDataByFormId(String formId, ClientMacro clientMacro) {
        String defaultDataId = "-1";
        CustomFormVO formVO = getCustomFormByFormId(formId, defaultDataId, clientMacro);
        setFormDefaultData(formVO);
        return formVO;
    }

    @Override
    public CustomFormVO getEditDataByFormId(String formId, String dataId, ClientMacro clientMacro) {
        CustomFormVO formVO = getCustomFormByFormId(formId, dataId, clientMacro);
        setFormDefaultData(formVO);
        return formVO;
    }

    @Override
    public CustomFormVO getCopyDataByFormId(String formId, String dataId) {
        CustomFormVO formVO = getCustomFormByFormId(formId, dataId, null);
        setFormDefaultData(formVO);
        //复制数据后进行数据处理
        Long newId = idGenerator.nextId();
        formVO.getData().setId(newId);
        formVO.getData().setState(EDBEntityState.Added);
        //去掉旧数据关联到的附件信息，因为此场景无需复制附件 20230812 by ss
        for (DbField f : formVO.getTableInfo().getFieldList()) {
            if (f.getEleType().equals("8") || f.getEleType().equals("9")) {
                formVO.getData().setRefData(f.getCode(), "");
            }
        }
        return formVO;
    }

    void setFormDefaultData(CustomFormVO formVO) {
        if (StringUtils.isEmpty(formVO.getId())) {
            return;
        }
        List<DbField> collect = formVO.getTableInfo().getFieldList().stream().filter(m -> m.getEleType().equals("8") || m.getEleType().equals("9")).collect(Collectors.toList());

        for (DbField field : collect) {
            if (StringUtils.isEmpty(formVO.getData().get(field.getCode()))) {
                //formVO.getData().put(field.getCode(), idGenerator.nextId());
                formVO.getData().put(field.getCode(), String.format("%s,%s", formVO.getTableInfo().getId(), field.getCode()));
            }
        }
    }

    @SneakyThrows
    IFormTrigger getFormTriggerInstance(DmCustomFormEntity dmCustomform) {
        if (StringUtils.isEmpty(dmCustomform.getTrigger_path())) {
            return null;
        } else {
            IFormTrigger trigger = customFormServiceAdapter.getFormTriggerByPath(dmCustomform.getTrigger_path());
            return trigger;
        }
    }

    DmCustomFormEntity selectById(String formId) {
        Long id = Long.valueOf(formId);
        DbCollection collection = dataAccess.queryById(DmCustomFormEntity.TABLE_CODE, id);
        if (collection.getTotalCount() == 1) {
            return collection.getGenericData(DmCustomFormEntity.class).get(0);
        }
        return null;
    }

    @SneakyThrows
    private CustomFormVO getCustomFormByFormId(String formId, String dataId, ClientMacro clientMacro) {
        CustomFormVO formVO = new CustomFormVO();
        formVO.setReadonly(false);
        if (StringUtils.isEmpty(formId)) {
            throw new WarnCommonException("formId 不能为空，请检查按钮是否配置了表单");
        }
        DmCustomFormEntity dmCustomform = selectById(formId);
        if (null == dmCustomform) {
            throw new WarnCommonException("未找到表单" + formId);
        }
        if (clientMacro == null) {
            clientMacro = new ClientMacro();
        }
        IFormTrigger formTrigger = getFormTriggerInstance(dmCustomform);
        formVO.setId(formId);
        formVO.setExtendScript(dmCustomform.getExtend_script());
        formVO.setName(dmCustomform.getForm_name());
        formVO.setForm_config(dmCustomform.getForm_config());
        //根据字段获取列信息
        QueryOption queryOption = new QueryOption(dmCustomform.getTable_code());
        //表单查询开启字段权限控制
        queryOption.setEnableColAccessControl(true);
        queryOption.setFixField(dmCustomform.getCols());
        if (StringUtils.isEmpty(queryOption.getFixField())) {
            queryOption.setFixField("id");
        }
        queryOption.setTableFilter(new TableFilter("id", dataId));
        queryOption.setQueryMode(2);
        queryOption.setPageIndex(0);
        DbCollection collection = dataAccess.queryAllData(queryOption);
        // 合并 filedInfo和 tableInfo 方便客户端进行渲染 20240602 lubaase
        collection.getTableInfo().setFieldList(customFormDataService.getFormFieldSetting(collection.getTableInfo().getFieldList(), dmCustomform.getField_info()));
        formVO.setTableInfo(collection.getTableInfo());

        formVO.setLayout(dmCustomform.getData());
        formVO.setRule(formRuleService.getFormRuleById(dmCustomform.getId()));
        formVO.setBtns(formRuleService.getFormButtonListById(formId));
        if (collection.getData().size() == 1) {
            //edit
            collection.getData().get(0).setState(EDBEntityState.Modified);
            formVO.setData(collection.getData().get(0));
        } else {
            //add
            // TODO 临时这么处理，后续要迁移走
            DbEntity entity = new DbEntity();
            entity.setState(EDBEntityState.Added);
            entity.setId(idGenerator.nextId());
            for (DbField field : collection.getTableInfo().getFieldList()) {
                String colDefault = field.getColDefault();
                //不可见字段和只读字段不处理默认值
                if (StringUtils.isEmpty(colDefault) || field.getVisible() < EAccessGrade.NewToWrite.getIndex()) {
                    continue;
                }
                String[] defaultValue = getColumnDefaultValue(colDefault, clientMacro);
                entity.put(field.getCode(), defaultValue[0]);
                if (colDefault.indexOf(",") > 0) {
                    entity.setRefData(field.getCode(), defaultValue[1]);
                }
            }
            if (formTrigger != null) {
                formTrigger.initDefaultValue(entity, clientMacro);
            }
            formVO.setData(entity);
        }
        if (formTrigger != null) {
            formTrigger.beforeLoadForm(formVO, clientMacro);
        }
        return formVO;
    }

    private String[] getColumnDefaultValue(String colDefault, ClientMacro clientMacro) {
        String[] defaultValues = new String[2];
        Boolean multiValued = colDefault.indexOf(",") > 0;
        String[] tmpValues = colDefault.split(",");
        defaultValues[0] = "";
        defaultValues[1] = "";
        if (colDefault.startsWith(ClientMacro.clientMacroPre)) {
            if (clientMacro.containsKey(tmpValues[0])) {
                defaultValues[0] = clientMacro.get(tmpValues[0]);
                if (multiValued && clientMacro.containsKey(tmpValues[1])) {
                    defaultValues[1] = clientMacro.get(tmpValues[1]);
                }
            }
        } else if (colDefault.startsWith(ClientMacro.serverMacroPre)) {
            defaultValues[0] = serverMacroService.getServerMacroByKey(tmpValues[0]);
            if (multiValued && defaultValues[0] != null) {
                defaultValues[1] = serverMacroService.getServerMacroByKey(tmpValues[1]);
            }
        } else {
            defaultValues[0] = tmpValues[0];
            if (multiValued) {
                defaultValues[1] = tmpValues[1];
            }
        }
        return defaultValues;
    }


    @SneakyThrows
    @Override
    public ChildTableDataVO getFormChildTableData(HashMap<String, String> mapParam) {
        return getFormChildTableData(mapParam, false);
    }

    @Override
    public ChildTableDataVO getFormChildAllTableData(HashMap<String, String> mapParam) {
        return getFormChildTableData(mapParam, true);
    }

    @SneakyThrows
    private ChildTableDataVO getFormChildTableData(HashMap<String, String> mapParam, Boolean allData) {
        // searchParam
        Long dataId = Long.parseLong(checkAndGetParam("dataId", mapParam));
        String serialNum = checkAndGetParam("serialNum", mapParam);
        Long formId = Long.parseLong(checkAndGetParam("formId", mapParam));
        Integer pageSize = 0, pageIndex = 0;
        if (mapParam.containsKey("pageIndex") && !org.apache.commons.lang3.StringUtils.isEmpty(mapParam.get("pageIndex"))) {
            pageIndex = Math.max(1, Integer.parseInt(mapParam.get("pageIndex")));
        }
        if (mapParam.containsKey("pageSize") && !org.apache.commons.lang3.StringUtils.isEmpty(mapParam.get("pageSize"))) {
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
        if (org.apache.commons.lang3.StringUtils.isBlank(formEntity.getChild_table()) || childTable == null || childTable.getQueryOption() == null) {
            throw new InvokeCommonException("表单未设置子表配置信息，请检查");
        }
        QueryOption childQuery = childTable.getQueryOption();

        ChildTableDataVO childTableDataVO = new ChildTableDataVO();
        //如果设置了查询id和查询类型则调用扩展服务查询
        if (childQuery.getServerId() != null && childQuery.getServerId() > 0L && childQuery.getQueryType() == 4) {
            LoadSubTableDataService service = customFormServiceAdapter.getSubTableDataServiceById(mainTableId.toString(), childQuery.getServerId().toString());
            if (service != null) {
                childTableDataVO.setDbCollection(service.loadChildTableData(dataId.toString()));
                childTableDataVO.setSearch(null);
                return childTableDataVO;
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
            if (childTable.getQueryOption().getTableFilter() != null && !org.apache.commons.lang3.StringUtils.isEmpty(childTable.getQueryOption().getTableFilter().getFilterName())) {
                filterWrapper.addFilter(childTable.getQueryOption().getTableFilter());
            }
            QueryOption queryOption = childTable.getQueryOption();
            if (org.apache.commons.lang3.StringUtils.isEmpty(queryOption.getFixField())) {
                queryOption.setFixField("*");
            }
            TableFilter searchFilter = searchCondition2TableFilterService.convertToTableFilter(searchParamStr);
            if (searchFilter != null) {
                filterWrapper.addFilter(searchFilter);
            }
            queryOption.setTableFilter(filterWrapper.build());

            if (pageIndex > 0) {
                queryOption.setPageIndex(pageIndex);
            }
            if (pageSize > 0) {
                queryOption.setPageSize(pageSize);
            }
            if (allData) {
                queryOption.setQueryMode(2);
            }
            DbCollection collChild = dataAccess.query(queryOption);
            //如果指定了查询顺序则调整列顺序
            if (!org.apache.commons.lang3.StringUtils.isEmpty(queryOption.getFixField()) && !queryOption.getFixField().equals("*")) {
                int i = 1;
                for (DbField f : collChild.getTableInfo().getFieldList()) {
                    f.setOrderId(i++);
                }
            }
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

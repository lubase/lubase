package com.lubase.core.service.exportmanage.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.entity.DmTableRelationEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.service.AppFuncDataService;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.core.service.exportmanage.ImportService;
import com.lubase.model.*;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.remote.DeptInfoServiceImpl;
import com.lubase.orm.extend.remote.UserInfoByCodeServiceImpl;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.EColumnType;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@ConditionalOnProperty(name = "lubase.import.disable-default-service", havingValue = "0", matchIfMissing = true)
public class ImportServiceImpl implements ImportService {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    AppFuncDataService appFuncDataService;

    @Autowired
    CustomFormDataService customFormDataService;
    @Autowired
    UserInfoByCodeServiceImpl userInfoByCodeService;
    @Autowired
    DeptInfoServiceImpl deptInfoService;

    private String[] notImportField = new String[]{"id", "create_by", "create_time", "update_time", "update_by"};

    @SneakyThrows
    @Override
    public void getExportMainPageTemplate(String pageId, String clientMacroStr, HttpServletResponse response) {
        if (StringUtils.isEmpty(pageId)) {
            throw new ParameterNotFoundException("pageId");
        }
        SsPageEntity pageEntity = appFuncDataService.getPageById(pageId);
        QueryOption queryOption = JSON.parseObject(pageEntity.getGrid_query(), QueryOption.class);
        DbCollection importCollect = dataAccess.getEmptyData(queryOption.getTableName());
        exportTemplateByDbCollection(importCollect, response);
    }

    @SneakyThrows
    @Override
    public void getExportSubTableTemplate(String formId, String serialNum, String clientMacroStr, HttpServletResponse response) {
        if (StringUtils.isEmpty(formId) || StringUtils.isEmpty(serialNum)) {
            throw new ParameterNotFoundException("formId or serialNum");
        }
        //1、获取表单子表查询对象
        DmCustomFormEntity formEntity = customFormDataService.selectById(formId);
        //2、获取表单主表与子表关联关系
        ChildTableSetting childTable = customFormDataService.getChildTableFromSettingStr(formEntity.getChild_table(), serialNum);
        if (org.apache.commons.lang3.StringUtils.isBlank(formEntity.getChild_table()) || childTable == null || childTable.getQueryOption() == null) {
            throw new WarnCommonException("表单未设置子表配置信息，请检查");
        }
        DbTable table = dataAccess.initTableInfoByTableId(childTable.getQueryOption().getTableId());
        DbCollection coll = dataAccess.getEmptyData(table.getCode());
        exportTemplateByDbCollection(coll, response);
    }

    @SneakyThrows
    private void exportTemplateByDbCollection(DbCollection importCollect, HttpServletResponse response) {
        //表头
        List<List<String>> headList = new ArrayList<List<String>>();
        List<List<String>> contentList = new ArrayList<List<String>>();

        for (DbField filed : importCollect.tableInfo.getFieldList()) {
            //跳过不导出的列
            if (!isImport(filed)) {
                continue;
            }
            List<String> tmpName = new ArrayList();
            tmpName.add("导入模板提示");
            tmpName.add(GetTip(filed));
            tmpName.add(filed.getName());
            headList.add(tmpName);
        }

        //excel文件名
        final String FILENAME = String.format("%s-%s", importCollect.tableInfo.getName(), "模板");
        //sheetName
        final String SHEETNAME = importCollect.tableInfo.getName() + "-" + LocalDateTime.now().toString().substring(0, 10);
        try {
            //表头样式策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            //初始化表格样式
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(FILENAME, "UTF-8").replaceAll("\\+", "%20");
            //响应首部 Access-Control-Expose-Headers 就是控制“暴露”的开关，它列出了哪些首部可以作为响应的一部分暴露给外部。
            //此处设置了开放Content-Disposition，前端可获取该响应参数获取文件名称
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream()).autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(horizontalCellStyleStrategy).sheet(SHEETNAME).head(headList).doWrite(contentList);
        } catch (IOException e) { //下载失败情况的处理
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    private Boolean isImport(DbField filed) {
        //跳过不导出的列
        if (Arrays.stream(notImportField).anyMatch(d -> d.equals(filed.getCode()))) {
            return false;
        }
        if (filed.getEleType().equals(EColumnType.Document.getIndex().toString())
                || filed.getEleType().equals(EColumnType.Image.getIndex().toString())) {
            return false;
        }
        if (filed.getFieldAccess().equals(EAccessGrade.Invisible)) {
            return false;
        }
        return true;
    }

    private String GetTip(DbField filed) {
        String tip = "";
        if (filed.getEleType().equals(EColumnType.Numeric.getIndex().toString()) || filed.getEleType().equals(EColumnType.Int.getIndex().toString())) {
            tip = "请填写数字,例：12";
        } else if (filed.getEleType().equals(EColumnType.Date.getIndex().toString())) {
            String Format = "yyyy-MM-dd HH:mm:ss";
            if (filed.getDataFormat() != null) {
                Format = filed.getDataFormat();
            }
            tip = "请填日期,格式：" + Format;
        } else if (filed.getEleType().equals(EColumnType.Boolean.getIndex().toString())) {
            tip = "请填写true或者false";
        } else if (filed.getEleType().equals(EColumnType.Lookup.getIndex().toString())) {
            tip = "关联字段,如需导入请属入关联表相应主键";

        } else if (filed.getEleType().equals("10")) {
            String componentPath = filed.getServiceName();
            if (componentPath.equals("userColumnServiceImpl")) {
                tip = "请输入用户,例：张三(工号)或者工号";
            } else if (componentPath.equals("deptInfoServiceImpl")) {
                tip = "请输入部门名称";
            } else {
                tip = "参考服务列：" + componentPath + "配置";
            }
        } else if (filed.getEleType().equals(EColumnType.CodeData.getIndex().toString())) {
            if (null != filed.getCodeTypeId()) {
                List<DbCode> list = dataAccess.getCodeListByTypeId(filed.getCodeTypeId());
                List<String> TmpCodeList = new ArrayList<>();
                for (var dbcode : list) {
                    TmpCodeList.add(dbcode.getName());
                }
                tip = "关联码表：" + StringUtils.join(TmpCodeList, ';');
            }
        } else {
            tip = "请输入字符串，例：张三";
        }
        return tip;
    }

    @SneakyThrows
    @Override
    public boolean importMainPageTable(String pageId, String clientMacroStr, MultipartFile file) {
        if (StringUtils.isEmpty(pageId)) {
            throw new ParameterNotFoundException("pageId");
        }
        SsPageEntity pageEntity = appFuncDataService.getPageById(pageId);
        String tableName = JSON.parseObject(pageEntity.getGrid_query(), QueryOption.class).getTableName();
        return importTableData(tableName, file, new HashMap<>());
    }

    @SneakyThrows
    @Override
    public boolean importSubPageTable(String pageId, String formId, String serialNum, String dataId, String clientMacroStr, MultipartFile file) {
        if (StringUtils.isEmpty(formId) || StringUtils.isEmpty(serialNum) || StringUtils.isEmpty(pageId) || StringUtils.isEmpty(dataId)) {
            throw new ParameterNotFoundException("formId or serialNum or pageId or dataId");
        }
        //1、获取表单子表查询对象
        DmCustomFormEntity formEntity = customFormDataService.selectById(formId);
        //2、获取表单主表与子表关联关系
        ChildTableSetting childTable = customFormDataService.getChildTableFromSettingStr(formEntity.getChild_table(), serialNum);

        Long childTableId = childTable.getQueryOption().getTableId();
        Long mainTableId = formEntity.getTable_id();
        //查询主从表对应关系
        DbEntity tableRelation = getTableRelation(mainTableId, childTableId);
        if (tableRelation == null) {
            throw new WarnCommonException("主从表关系设置不正确，请检查");
        }
        String fkColumn = tableRelation.get("fk_column_code").toString();
        if (StringUtils.isBlank(formEntity.getChild_table()) || childTable.getQueryOption() == null) {
            throw new WarnCommonException("表单未设置子表配置信息，请检查");
        }
        DbTable table = dataAccess.initTableInfoByTableId(childTable.getQueryOption().getTableId());
        if (table == null) {
            throw new WarnCommonException("子表" + childTable.getTitle() + childTable.getQueryOption().getTableName() + "已经删除请调整表单");
        }
        HashMap<String, Object> mapDefaultValue = new HashMap<>();
        mapDefaultValue.put(fkColumn, dataId);
        return importTableData(table.getCode(), file, mapDefaultValue);
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

    @SneakyThrows
    private boolean importTableData(String tableName, MultipartFile file, HashMap<String, Object> defaultValueMap) {
        InputStream inputStream = null;
        inputStream = file.getInputStream();
        DbCollection importCollect = dataAccess.getEmptyData(tableName);
        //码表
        List<String> codes = new ArrayList<>();
        //类型
        List<String> eleTypes = new ArrayList<>();
        //表头
        List<List<String>> headList = new ArrayList<>();
        //关联码表
        Map<String, String> codeTypeMap = MapUtils.newHashMap();
        //系统服务表
        Map<String, String> sysTemMap = MapUtils.newHashMap();
        for (DbField filed : importCollect.tableInfo.getFieldList()) {
            //跳过不导出的列
            if (!isImport(filed)) {
                continue;
            }
            List<String> tmp = new ArrayList<>(1);
            tmp.add("导入模板提示");
            tmp.add(GetTip(filed));
            tmp.add(filed.getName());
            codes.add(filed.getCode());
            eleTypes.add(filed.getEleType());
            if (filed.getEleType().equals(EColumnType.CodeData.getIndex().toString())) {
                codeTypeMap.put(filed.getCode(), filed.getCodeTypeId());
            } else if (filed.getEleType().equals("10")) {
                sysTemMap.put(filed.getCode(), filed.getServiceName());
            }
            headList.add(tmp);
        }
        try {
            List<LinkedHashMap<String, String>> objectList = EasyExcel.read(inputStream).head(headList).doReadAllSync();
            List<DbEntity> entityList = new ArrayList<>();
            for (LinkedHashMap<String, String> stringStringLinkedHashMap : objectList) {
                DbEntity en = new DbEntity();
                for (String key : defaultValueMap.keySet()) {
                    en.put(key, defaultValueMap.get(key));
                }
                for (int line = 0; line < stringStringLinkedHashMap.size(); line++) {
                    String cell = TypeConverterUtils.object2String(stringStringLinkedHashMap.get(line));
                    if (cell != null) {
                        if (eleTypes.get(line).equals(EColumnType.CodeData.getIndex().toString())) {
                            List<DbCode> list = dataAccess.getCodeListByTypeId(codeTypeMap.get(codes.get(line)));
                            for (var dbcode : list) {
                                if (cell.equals(dbcode.getName())) {
                                    cell = dbcode.getCode();
                                }
                            }
                        } else if (eleTypes.get(line).equals("10")) {
                            //用户扩展
                            if ("userColumnServiceImpl".equals(sysTemMap.get(codes.get(line)))) {
                                DbEntity userInfo = userInfoByCodeService.getCacheDataByKey(cell);
                                log.info("进入userColumnServiceImpl");

                                if (userInfo.get("id") != null) {
                                    cell = userInfo.get("id").toString();
                                }
                            }
                            //组织扩展
                            else if ("userOrgIdColumnServiceImpl".equals(sysTemMap.get(codes.get(line)))) {
                                QueryOption queryOption = new QueryOption("sa_organization");
                                queryOption.setTableFilter(new TableFilter("org_name", cell));
                                DbCollection db = deptInfoService.getDataByFilter(queryOption, "");
                                if (!db.getData().isEmpty()) {
                                    cell = db.getData().get(0).get("id").toString();
                                }
                            }
                        }
                    }
                    en.put(codes.get(line), cell);
                }

                en.setState(EDBEntityState.Added);
                entityList.add(en);
            }
            importCollect.setData(entityList);
            importCollect.setEnableTableTrigger(false);
            int i = dataAccess.update(importCollect);
            log.info("导入成功{}行", i);
        } catch (Exception e) {
            //导入失败情况的处理
            log.error("导入失败:", e);
            throw new WarnCommonException("导入失败" + e.getMessage());
        }
        return true;
    }
}

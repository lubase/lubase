package com.lubase.orm.service.update.impl;

import com.alibaba.fastjson.JSON;
import com.googlecode.aviator.AviatorEvaluator;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.query.DataAccessQueryCoreService;
import com.lubase.orm.service.update.GetAvgValue;
import com.lubase.orm.service.update.GetNextIndex;
import com.lubase.orm.service.update.GetSumValue;
import com.lubase.orm.service.update.UpdateTriggerService;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Order(5)
public class UpdateCalcColumnTriggerImpl implements UpdateTriggerService {
    private UpdateCalcColumnTriggerImpl() {
        // 构造函数中增加函数：计算表达式中 a_w 函数，用于获取固定位数的流水号，调用方式 a_w(currentIndex,length)
        AviatorEvaluator.addFunction(new GetNextIndex());
        AviatorEvaluator.addFunction(new GetSumValue());
        AviatorEvaluator.addFunction(new GetAvgValue());
    }

    /**
     * 编号列标识。如果有此标识则代表是编号列
     */
    private final String indexColumnTag = "a_w(a_w_index";

    /**
     * 子表标识
     */
    private final String childTable = "ct,";

    @Autowired
    DataAccessQueryCoreService queryCoreService;

    @Override
    public void beforeUpdate(DbCollection collection) {
        for (DbField field : collection.getTableInfo().getFieldList()) {
            if (StringUtils.isEmpty(field.getExpression())) {
                continue;
            }
            if (!field.getTableId().equals(collection.getTableInfo().getId())) {
                continue;
            }
            field.setVisible(4);
            field.setRight(4);

            String express = field.getExpression();
            Integer currentIndex = 0;
            //子表数据
            String ctData = "";
            String ctTableCode = "", ctColumnCode = "", fkColumn = "";
            if (express.contains(indexColumnTag)) {
                //有新增数据时 才计算编号列数据
                if (collection.getData().stream().noneMatch(d -> d.getDataState().equals(EDBEntityState.Added))) {
                    continue;
                }
                String settings = express.substring(0, express.indexOf("|"));
                express = express.substring(express.indexOf("|") + 1);
                String[] settingArray = settings.split(",");
                if (settingArray.length != 3) {
                    log.error(String.format("字段%s.%s(%s)计算表达式设置不正确", field.getTableCode(), field.getCode(), field.getName()));
                    continue;
                }
                Integer preLength = Integer.parseInt(settingArray[0]);
                Integer indexLength = Integer.parseInt(settingArray[1]);
                Integer minIndex = Integer.parseInt(settingArray[2]);
                String existsValue = getMaxValueFromDatabase(field);
                // 计算当前最大的流水号
                if (existsValue == null) {
                    currentIndex = minIndex - 1;
                } else {
                    currentIndex = Integer.parseInt(existsValue.substring(preLength).substring(0, indexLength));
                }
            } else if (express.contains(childTable)) {
                //格式： ct,table.code,fkColumn|express
                String settings = express.substring(0, express.indexOf("|"));
                express = express.substring(express.indexOf("|") + 1);
                String[] settingArray = settings.split(",");
                if (settingArray.length != 3 || !settingArray[1].contains(".")) {
                    log.error(String.format("字段%s.%s(%s)计算表达式设置不正确", field.getTableCode(), field.getCode(), field.getName()));
                    continue;
                }
                ctTableCode = settingArray[1].split("\\.")[0];
                ctColumnCode = settingArray[1].split("\\.")[1];
                fkColumn = settingArray[2];

            }
            DbEntity newEntity = null;
            try {
                for (DbEntity entity : collection.getData()) {
                    if (entity.getDataState().equals(EDBEntityState.UnChanged) || entity.getDataState().equals(EDBEntityState.Deleted)) {
                        continue;
                    }
                    // 编号列不执行更新计算
                    if (express.contains(indexColumnTag) && entity.equals(EDBEntityState.Modified)) {
                        continue;
                    }
                    //如果配置了子表则计算子表数据
                    if (!StringUtils.isEmpty(ctTableCode)) {
                        ctData = getChildTableData(ctTableCode, ctColumnCode, fkColumn, entity.getId());
                    }
                    newEntity = new DbEntity();
                    newEntity.cloneFromNewEntity(entity);
                    newEntity.remove(field.getCode());
                    newEntity.put("a_w_index", currentIndex);
                    newEntity.put("a_w_ct_data", ctData);
                    Object result = AviatorEvaluator.execute(express, newEntity);
                    entity.put(field.getCode(), result);
                    currentIndex++;
                }
            } catch (Exception exception) {
                log.error(String.format("计算字段计算错误，表：%s 字段：%s，数据：", field.getTableCode(), field.getCode(), JSON.toJSONString(newEntity)), exception);
            }
        }
    }

    private String getMaxValueFromDatabase(DbField field) {
        QueryOption queryOption = new QueryOption(field.getTableCode());
        queryOption.setFixField(field.getCode());
        queryOption.setPageIndex(1);
        queryOption.setPageSize(1);
        queryOption.setSortField("id desc");
        DbCollection existsColl = queryCoreService.query(queryOption, false);
        if (existsColl.getData().size() > 0) {
            return TypeConverterUtils.object2String(existsColl.getData().get(0).get(field.getCode()), null);
        } else {
            return null;
        }
    }

    private String getChildTableData(String ctTableCode, String ctColumnCode, String fkColumn, Long mainDataId) {
        //查询子表数据
        QueryOption queryOption = new QueryOption(ctTableCode);
        queryOption.setFixField(ctColumnCode);
        queryOption.setTableFilter(new TableFilter(fkColumn, mainDataId, EOperateMode.Equals));
        queryOption.setQueryMode(2);
        DbCollection coll = queryCoreService.query(queryOption, false);
        List<String> resultList = new ArrayList<>();
        for (DbEntity entity : coll.getData()) {
            String tmpValue = TypeConverterUtils.object2String(entity.get(ctColumnCode), "");
            if (!tmpValue.isEmpty()) {
                resultList.add(tmpValue);
            }
        }
        if (resultList.size() > 0) {
            return String.join(",", resultList);
        } else {
            return "";
        }
    }
}

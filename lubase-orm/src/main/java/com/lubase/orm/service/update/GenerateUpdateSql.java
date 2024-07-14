package com.lubase.orm.service.update;

import com.lubase.orm.model.EColumnType;
import com.lubase.orm.model.SqlEntity;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import com.lubase.model.EAccessGrade;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * 根据修改信息生成更新语句
 *
 * @author A
 */
@Component
public class GenerateUpdateSql {

    /**
     * 新增时 必须写入的特殊字段
     */
    private final ArrayList<String> AddSceneSpecialFields = new ArrayList<>();
    /**
     * 更新时 必须更新的特殊字段
     */
    private final ArrayList<String> EditSceneSpecialFields = new ArrayList<>();

    public GenerateUpdateSql() {
        AddSceneSpecialFields.add("create_by");
        AddSceneSpecialFields.add("create_time");
        EditSceneSpecialFields.add("update_by");
        EditSceneSpecialFields.add("update_time");
    }

    public SqlEntity addSql(DbEntity entity, DbTable tableInfo, SqlEntity sqlEntity, Boolean isServer) {
        StringBuilder sbInsertCols = new StringBuilder();
        StringBuilder sbValueCols = new StringBuilder();
        for (DbField col : tableInfo.getFieldList()) {
            if (!isNeedAdd(col, entity, tableInfo, isServer)) {
                continue;
            }
            Object colValue = entity.get(col.getCode());
            if (colValue == null) {
                if (col.getEleType().equals(EColumnType.Text.getStringValue())) {
                    //字符串类型新增时接收null值，入库为 空字符串
                    colValue = "";
                } else {
                    continue;
                }
            }
            if (col.getEleType().equals(EColumnType.Numeric.getStringValue()) || col.getEleType().equals(EColumnType.Int.getStringValue())
                    || col.getEleType().equals(EColumnType.Lookup.getStringValue()) || col.getEleType().equals(EColumnType.BigInt.getStringValue())) {
                // 整数和小数时,关联数据表，如果值是“” 则不存入
                if (StringUtils.isEmpty(colValue.toString())) {
                    continue;
                }
            } else if (col.getEleType().equals(EColumnType.Date.getStringValue())) {
                String Format = "yyyy-MM-dd HH:mm:ss";
                if (col.getDataFormat() != null) {
                    Format = col.getDataFormat();
                }
                colValue = TypeConverterUtils.object2LocalDateTime2String(colValue, Format);
            } else if (col.getEleType().equals(EColumnType.Boolean.getStringValue())) {
                colValue = ("1".equals(colValue.toString()) || "true".equalsIgnoreCase(colValue.toString())) ? 1 : 0;
            }
            String paramName = sqlEntity.addParam(colValue);
            sbInsertCols.append(col.getCode()).append(",");
            sbValueCols.append(paramName).append(",");
        }

        String strSql = sbInsertCols.toString().substring(0, sbInsertCols.toString().length() - 1);
        if (!StringUtils.isEmpty(strSql)) {
            String val = sbValueCols.toString().substring(0, sbValueCols.toString().length() - 1);
            strSql = String.format("insert into %s (%s) values (%s);", tableInfo.getCode(), strSql, val);
            sqlEntity.setSqlStr(strSql);
        }
        return sqlEntity;
    }

    boolean isNeedAdd(DbField col, DbEntity entity, DbTable tableInfo, Boolean isServer) {
        //此方法的判断各个分支逻辑判断顺序调整需慎重！！！！

        //1、非主表字段不add 新增时update_by、update_time 默认为空
        if (!col.getTableCode().equals(tableInfo.getCode()) || EditSceneSpecialFields.contains(col.getCode())) {
            return false;
        }
        //2、新增时特殊字段处理。 id 永远是可以写入的字段
        if (col.isPrimaryKey() || AddSceneSpecialFields.contains(col.getCode())) {
            return true;
        }
        //3、客户端更新，字段没有写权限
        //客户端更新模式下，不可见字段是没有权限进行新增的 ss 022-06-20
        if (!isServer && col.getFieldAccess() != null
                && !col.getFieldAccess().equals(EAccessGrade.NewToWrite)
                && !col.getFieldAccess().equals(EAccessGrade.Write)) {
            return false;
        }
        //4、数据对象不包含字段无需更新
        if (!entity.containsKey(col.getCode())) {
            return false;
        }
        return true;
    }

    public SqlEntity updateSql(DbEntity entity, DbTable tableInfo, Boolean isServer, SqlEntity sqlEntity) {
        StringBuilder sbCols = new StringBuilder();
        for (DbField col : tableInfo.getFieldList()) {
            if (!isNeedUpdate(col, entity, tableInfo, isServer)) {
                continue;
            }
            Object colValue = entity.get(col.getCode());
            if (col.getEleType().equals(EColumnType.Date.getIndex().toString())) {
                String Format = "yyyy-MM-dd HH:mm:ss";
                if (col.getDataFormat() != null) {
                    Format = col.getDataFormat();
                }
                String dtvalue = TypeConverterUtils.object2LocalDateTime2String(colValue, Format);
                colValue = dtvalue;
            } else if (col.getEleType().equals(EColumnType.Boolean.getStringValue())) {
                if (StringUtils.isEmpty(colValue)) {
                    colValue = 0;
                } else if (colValue instanceof Boolean) {
                    colValue = ((Boolean) colValue) ? 1 : 0;
                } else {
                    colValue = "1".equals(colValue.toString()) ? 1 : 0;
                }
            } else if (col.getEleType().equals(EColumnType.Numeric.getStringValue()) || col.getEleType().equals(EColumnType.Int.getStringValue())
                    || col.getEleType().equals(EColumnType.Lookup.getStringValue()) || col.getEleType().equals(EColumnType.BigInt.getStringValue())) {
                if (StringUtils.isEmpty(colValue)) {
                    colValue = null;
                }
            }
            if (colValue == null) {
                // 不允许为空的字符串 如果更新为null，需要设置为更新为空字符串，避免数据库中非空索引校验失败
                if (col.getIsNull() == 0 && col.getEleType().equals(EColumnType.Text.getStringValue())) {
                    colValue = "";
                }
            }
            String paramName = sqlEntity.addParam(colValue);
            sbCols.append(String.format("%s = %s,", col.getCode(), paramName));
        }
        String strSql = null;
        if (sbCols.length() > 0) {
            strSql = sbCols.toString().substring(0, sbCols.toString().length() - 1);
        }
        if (!StringUtils.isEmpty(strSql)) {
            String paramId = sqlEntity.addParam(entity.getId());
            strSql = String.format("UPDATE %s SET %s WHERE id = %s;", tableInfo.getCode(), strSql, paramId);
            sqlEntity.setSqlStr(strSql);
        }
        return sqlEntity;
    }

    boolean isNeedUpdate(DbField col, DbEntity entity, DbTable tableInfo, Boolean isServer) {
        //此方法的判断各个分支逻辑判断顺序调整需慎重！！！！

        //1、主键不能更新
        if (col.isPrimaryKey() || AddSceneSpecialFields.contains(col.getCode())) {
            return false;
        }
        //2、非主表字段不更新
        if (!col.getTableCode().equals(tableInfo.getCode())) {
            return false;
        }
        //特殊字段处理
        if (EditSceneSpecialFields.contains(col.getCode())) {
            return true;
        }
        //3、客户端更新，字段没有写权限
        if (!isServer && col.getFieldAccess() != null && !col.getFieldAccess().equals(EAccessGrade.Write)) {
            return false;
        }
        //4、数据对象不包含字段无需更新
        if (!entity.containsKey(col.getCode())) {
            return false;
        }
        //数据对象中字段值没有变化
        if (isServer && !entity.isPropertyChanged(col.getCode())) {
            return false;
        }
        return true;
    }

    public SqlEntity deleteSql(DbEntity entity, DbTable tableInfo, SqlEntity sqlEntity) {
        if (!StringUtils.isEmpty(entity.getId())) {
            String paramName = sqlEntity.addParam(entity.getId());
            // 注意 tableInfo对象对服务端对象，所以此处拼接sql并不会带来sql注入风险，可以放心使用
            String sql = String.format("delete from %s where id=%s;", tableInfo.getCode(), paramName);
            sqlEntity.setSqlStr(sql);
        }
        return sqlEntity;
    }
}


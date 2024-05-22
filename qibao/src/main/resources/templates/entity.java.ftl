package ${package.Entity};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>
import com.lcp.core.util.TypeConverterUtils;
<#if swagger>
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
    import lombok.Getter;
    import lombok.Setter;
    <#if chainModel>
        import lombok.experimental.Accessors;
    </#if>
</#if>

/**
* hello custom
* <p>
    * ${table.comment!}
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if entityLombokModel>
    @Getter
    @Setter
    <#if chainModel>
        @Accessors(chain = true)
    </#if>
</#if>
<#if table.convert>
    @TableName("${schemaName}${table.name}")
</#if>
<#if swagger>
    @ApiModel(value = "${entity}对象", description = "${table.comment!}")
</#if>
<#if superEntityClass??>
    public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
    public class ${entity} extends Model<${entity}> {
<#elseif entitySerialVersionUID>
    public class ${entity} implements Serializable {
<#else>
    public class ${entity} {
</#if>
<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
public static final String TABLE_CODE = "${table.name}";
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    public static final String COL_${field.propertyName?upper_case} = "${field.propertyName?lower_case}";
</#list>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#continue >
    <#if field.propertyName == "id">
        <#continue >
    </#if>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if swagger>
            @ApiModelProperty("${field.comment}")
        <#else>
            /**
            * ${field.comment}
            */
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
            @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
        <#elseif idType??>
            @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
        <#elseif field.convert>
            @TableId("${field.annotationColumnName}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
            @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
        <#else>
            @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
        @TableField("${field.annotationColumnName}")
    </#if>
<#-- 乐观锁注解 -->
    <#if field.versionField>
        @Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if field.logicDeleteField>
        @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyName == "id">
            <#continue >
        </#if>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
        public ${field.propertyType} ${getprefix}${field.capitalName}() {
        <#if field.propertyType == "String">
            return TypeConverterUtils.object2String(super.get("${field.propertyName?lower_case}"));
        <#elseif field.propertyType == "Integer">
            return TypeConverterUtils.object2Integer(super.get("${field.propertyName?lower_case}"));
        <#elseif field.propertyType == "Boolean">
            return TypeConverterUtils.object2Boolean(super.get("${field.propertyName?lower_case}"));
        <#elseif field.propertyType == "Double">
            return TypeConverterUtils.object2Double(super.get("${field.propertyName?lower_case}"));
        <#elseif field.propertyType == "LocalDateTime">
            return TypeConverterUtils.object2LocalDateTime(super.get("${field.propertyName?lower_case}"));
        <#elseif field.propertyType == "Long">
            return TypeConverterUtils.object2Long(super.get("${field.propertyName?lower_case}"));
        <#else>
            return super.get("${field.propertyName?lower_case}");
        </#if>
        }

        <#if chainModel>
            public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        <#else>
            public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        </#if>
        super.put("${field.propertyName?lower_case}",${field.propertyName});
        <#if chainModel>
            return this;
        </#if>
        }
    </#list>
</#if>

<#if entityColumnConstant>
    <#list table.fields as field>
        public static final String ${field.name?upper_case} = "${field.name}";

    </#list>
</#if>
<#if activeRecord>
    @Override
    public Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

</#if>
<#if !entityLombokModel>
    @Override
    public String toString() {
    return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
            "${field.propertyName?upper_case}=" + get${field.capitalName}() +
        <#else>
            ", ${field.propertyName?upper_case}=" + get${field.capitalName}() +
        </#if>
    </#list>
    "}";
    }
</#if>
}

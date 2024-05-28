package com.lubase.model;

import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 通用的数据库实体
 *
 * @author A
 */
public class DbEntity extends HashMap<String, Object> {
    private static final long serialVersionUID = 5584605329383028413L;
    private final String stateHandle = "state";
    private final String idHandle = "id";
    /**
     * 原始数据
     */
    private HashMap<String, Object> originalData;

    private HashMap<String, String> refData;

    public DbEntity() {
        super.put(stateHandle, EDBEntityState.UnChanged.ordinal());
        refData = new HashMap<>();
    }

    public void setRefData(String code, String displayName) {
        this.refData.put(code + "NAME", displayName);
        acceptRefData();
    }

    public HashMap<String, String> getRefData() {
        if (this.refData != null && this.refData.size() > 0) {
            return this.refData;
        } else {
            if (super.containsKey("REFDATA") && super.get("REFDATA") instanceof HashMap) {
                HashMap<String, String> tmpRefData = (HashMap<String, String>) super.get("REFDATA");
                return tmpRefData;
            }
        }
        return new HashMap<>();
    }

    void acceptRefData() {
        super.put("REFDATA", this.refData);
    }

    /**
     * 获取数据的状态
     *
     * @return
     */
    public EDBEntityState getDataState() {
        Integer state = 0;
        if (super.containsKey(stateHandle)) {
            try {
                state = Integer.parseInt(super.get(stateHandle).toString());
            } catch (Exception e) {
            }
        }
        return EDBEntityState.fromIndex(state);
    }

    /**
     * update 请调用此方法
     *
     * @param state
     */
    public void setState(EDBEntityState state) {
        super.put(stateHandle, state.ordinal());
    }

    /**
     * 获取数据的主键ID
     *
     * @return
     */
    public Long getId() {
        if (super.containsKey(idHandle)) {
            if (StringUtils.isEmpty(super.get(idHandle))) {
                return null;
            }
            //sId为空 | null，报错,
            return Long.valueOf(super.get(idHandle).toString());
        }
        return null;
    }

    public void setId(Long id) {
        this.put(idHandle, id);
    }

    /**
     * 无痕模式增加，不会触发originalData
     *
     * @param key
     * @param value
     * @return
     */
    public Object putWithNoTrace(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public Object put(String key, Object value) {
        key = key.toLowerCase();
        EDBEntityState state = getDataState();
        if (state == EDBEntityState.Deleted) {
            //TODO：记录log
            return null;
        }
        if (isFirstChangeValue()) {
            setState(EDBEntityState.Modified);
            InitOriginalData();
        }
        if (isValueChange(key, value)) {
            return super.put(key, value);
        }
        return null;
    }

    boolean isFirstChangeValue() {
        EDBEntityState state = getDataState();
        if (state == EDBEntityState.UnChanged) {
            return true;
        }
        if (state == EDBEntityState.Modified && null == this.originalData) {
            return true;
        }
        return false;
    }

    boolean isValueChange(String key, Object value) {
        if (this.containsKey(key)) {
            if (null == value && null == this.get(key)) {
                return false;
            }
            if (null != this.get(key) && this.get(key).equals(value)) {
                return false;
            }
        }
        return true;
    }

    void InitOriginalData() {
        originalData = new HashMap<>();
        for (String key : super.keySet()) {
            this.originalData.put(key, super.get(key));
        }
    }

    /**
     * 从新对象赋值到自身
     *
     * @param entity
     */
    public void cloneFromNewEntity(DbEntity entity) {
        super.clear();
        for (String key : entity.keySet()) {
            super.put(key, entity.get(key));
        }
    }

    public void cloneFromNewEntity(HashMap<String, String> mapParams) {
        super.clear();
        for (String key : mapParams.keySet()) {
            super.put(key, mapParams.get(key));
        }
    }

    /**
     * 判断某个字段是否发生变化
     *
     * @param propertyName
     * @return
     */
    public boolean isPropertyChanged(String propertyName) {
        EDBEntityState state = getDataState();
        if (state == EDBEntityState.UnChanged) {
            return false;
        }
        if (state == EDBEntityState.Added || state == EDBEntityState.Deleted) {
            return true;
        }
        Object oldValue = null, newValue = null;
        if (this.originalData != null && this.originalData.containsKey(propertyName)) {
            oldValue = this.originalData.get(propertyName);
        }
        if (this.containsKey(propertyName)) {
            newValue = super.get(propertyName);
        }
        if (null != oldValue) {
            return !oldValue.equals(newValue);
        }
        if (null != newValue) {
            return !newValue.equals(oldValue);
        }
        return false;
    }

    public void acceptChange() {
        setState(EDBEntityState.UnChanged);
        this.originalData = null;
    }

    @Override
    public String toString() {
        return "DbEntity{" +
                ", originalData=" + originalData +
                ", super=" + super.toString() +
                '}';
    }
}

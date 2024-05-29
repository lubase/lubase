package com.lubase.wfengine.dao;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.EDBEntityState;
import com.lubase.wfengine.auto.entity.WfCmdEntity;
import com.lubase.wfengine.auto.entity.WfOInsEntity;
import com.lubase.wfengine.auto.entity.WfOperEntity;
import com.lubase.wfengine.model.ECmdType;
import com.lubase.wfengine.model.EProcessStatus;
import com.lubase.wfengine.model.OperatorUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class WfOperatorDao {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    WfCmdDao cmdDao;

    /**
     * 获取某个节点最后一个任务节点实例的处理人
     * @param finsId
     * @param taskId
     * @return
     */
    public List<DbEntity> getNodeRealProcessUserList(String finsId, String taskId) {
        QueryOption queryOption = new QueryOption(WfOInsEntity.TABLE_CODE);
        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        filterWrapper.eq(WfOInsEntity.COL_FINS_ID, finsId).eq(WfOInsEntity.COL_TASK_ID, taskId).eq(WfOInsEntity.COL_PROCESS_STATUS, EProcessStatus.Processed.getStatus());
        queryOption.setTableFilter(filterWrapper.build());
        queryOption.setFixField("tins_id,create_time,user_id,user_name,process_cmd_id");
        queryOption.setSortField("id desc");
        DbCollection collRealUser = dataAccess.queryAllData(queryOption);
        List<DbEntity> oInstList = collRealUser.getData();
        if (oInstList.size() > 0) {
            String lastTInsId = oInstList.get(0).get("tins_id").toString();
            List<DbEntity> lastNodeList = new ArrayList<>();
            for (DbEntity oIns : oInstList) {
                if (lastTInsId.equals(oIns.get("tins_id").toString())) {
                    lastNodeList.add(oIns);
                }
            }
            return lastNodeList;
        }
        return oInstList;
    }

    /**
     * 获取节点有效的处理人
     *
     * @param finsId
     * @param taskId
     * @return
     */
    public List<OperatorUserModel> getNodeValidProcessUserList(String finsId, String taskId) {
        List<DbEntity> allOInsList = getNodeRealProcessUserList(finsId, taskId);
        List<OperatorUserModel> userList = new ArrayList<>();
        for (DbEntity user : allOInsList) {
            String cmdId = TypeConverterUtils.object2String(user.get("process_cmd_id"), "");
            if (StringUtils.isEmpty(cmdId)) {
                continue;
            }
            //判断是否是退回
            if (isTransferCmd(cmdId)) {
                continue;
            }
            //去重
            if (userList.stream().anyMatch(u -> u.getUserId().equals(user.get("user_id").toString()))) {
                continue;
            }
            OperatorUserModel userModel = new OperatorUserModel();
            userModel.setUserId(user.get("user_id").toString());
            userModel.setUserName(user.get("user_name").toString());
            userList.add(userModel);
        }
        return userList;
    }

    private Boolean isTransferCmd(String cmdId) {
        WfCmdEntity cmdEntity = cmdDao.getWfCmdById(cmdId);
        if (cmdEntity != null && cmdEntity.getCmd_type().equals(ECmdType.Transfer.getType())) {
            return true;
        }
        return false;
    }

    public List<WfOperEntity> getOperatorByTaskId(String taskId) {
        QueryOption queryOption = new QueryOption(WfOperEntity.TABLE_CODE);
        queryOption.setTableFilter(new TableFilter(WfOperEntity.COL_TASK_ID, taskId));
        queryOption.setBuildLookupField(false);
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection.getGenericData(WfOperEntity.class);
    }

    public List<WfOInsEntity> getOInstanceByTInsId(String tInsId) {
        return getOInsCollectionByTInsId(tInsId).getGenericData(WfOInsEntity.class);
    }

    public DbCollection getOInsCollectionByTInsId(String tInsIds) {
        if (StringUtils.isEmpty(tInsIds)) {
            return null;
        }
        QueryOption queryOption = new QueryOption(WfOInsEntity.TABLE_CODE);
        if (tInsIds.contains(",")) {
            TableFilterWrapper filterWrapper = TableFilterWrapper.or();
            for (String tinsId : tInsIds.split(",")) {
                filterWrapper.eq("id", tinsId);
            }
            queryOption.setTableFilter(filterWrapper.build());
        } else {
            queryOption.setTableFilter(new TableFilter(WfOInsEntity.COL_TINS_ID, tInsIds));
        }
        queryOption.setBuildLookupField(false);
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection;
    }

    public WfOInsEntity getOInstanceById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Long longId = 0l;
        try {
            longId = TypeConverterUtils.object2Long(id);
        } catch (Exception ex) {
            return null;
        }
        DbCollection collection = dataAccess.queryById(WfOInsEntity.TABLE_CODE, longId);
        List<WfOInsEntity> list = collection.getGenericData(WfOInsEntity.class);
        return list.stream().findFirst().orElse(null);
    }

    public Integer updateOInsStatus(WfOInsEntity oIns) {
        if (oIns.getDataState() != EDBEntityState.Modified) {
            return 0;
        }
        DbCollection collection = dataAccess.getEmptyData(WfOInsEntity.TABLE_CODE);
        collection.getData().add(oIns);
        return dataAccess.update(collection);
    }
}

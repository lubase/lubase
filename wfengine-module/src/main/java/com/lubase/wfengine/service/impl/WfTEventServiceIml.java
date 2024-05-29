package com.lubase.wfengine.service.impl;

import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.dao.WfTEventDao;
import com.lubase.wfengine.model.EEventStatus;
import com.lubase.wfengine.service.WfTEventService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WfTEventServiceIml implements WfTEventService {

    @Autowired
    WfTEventDao eventDao;

    @Override
    public Integer eventProcessing(Long id) {
        return eventDao.updateEventStatus(id, EEventStatus.Processing);
    }

    @Override
    public Integer eventProcess(@NonNull Long id) {

        return eventDao.updateEventStatus(id, EEventStatus.Processed);
    }

    @Override
    public List<WfTEventEntity> getUnProcessEvent(Integer count) {
        return eventDao.getUnProcessEvent(count);
    }
}

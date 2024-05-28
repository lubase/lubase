package com.lubase.wfengine.model;

import lombok.Getter;
import lombok.Setter;

public class OpenEventModel {
    public OpenEventModel(EOpenEventType eventType, String serviceId, String dataId, String finsId) {
        this.openEventType = eventType;
        this.serviceId = serviceId;
        this.dataId = dataId;
        this.finsId = finsId;
    }

    @Getter
    private EOpenEventType openEventType;
    @Getter
    private String serviceId;
    @Getter
    private String dataId;

    @Getter
    private String finsId;

    @Getter
    @Setter
    private String taskId;

    @Getter
    @Setter
    private String taskInsId;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private String oInsId;

    @Getter
    @Setter
    private EApprovalStatus approvalStatus;
}

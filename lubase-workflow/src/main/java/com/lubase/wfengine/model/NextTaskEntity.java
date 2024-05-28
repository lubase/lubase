package com.lubase.wfengine.model;

import com.lubase.wfengine.auto.entity.WfLinkEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;

public class NextTaskEntity extends WfTaskEntity {
    /**
     * 流转到当前节点的连线
     */
    private WfLinkEntity fromLink;

    public void setFromLink(WfLinkEntity linkEntity) {
        this.fromLink = linkEntity;
    }

    public WfLinkEntity getFromLink() {
        return this.fromLink;
    }
}

package cn.edu.zju.isst.v2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.v2.net.CSTStatusInfo;

/**
 * Created by i308844 on 7/31/14.
 */
public abstract class CSTDataItem<T> implements Serializable {

    public final List<T> itemList = new ArrayList<T>();

    private CSTStatusInfo statusInfo;

    public CSTStatusInfo getStatusInfo() {
        return statusInfo;
    }

    public CSTDataItem<T> setStatusInfo(CSTStatusInfo statusInfo) {
        this.statusInfo = statusInfo;
        return this;
    }

}

package com.xym.laboratory.xmldigester;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
public class ViewCache {
    private List areaList = new ArrayList();

    public List getAreaList() {
        return areaList;
    }

    public void setAreaList(List areaList) {
        this.areaList = areaList;
    }

    // 供Digester调用的方法
    public void addArea(Area area) {
        this.areaList.add(area);
    }
}

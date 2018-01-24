package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 14.7.17.
 */

public class AreaItems {
    String areaId;
    String areaName;

    public AreaItems(String areaId, String areaName) {
        this.areaId = areaId;
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}

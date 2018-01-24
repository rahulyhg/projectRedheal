package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 10.4.17.
 */
public class AwardItem {

    String award;
    String awardId;
    String awardRedhealId;

    public AwardItem(String award, String awardId, String awardRedhealId) {
        this.award = award;
        this.awardId = awardId;
        this.awardRedhealId = awardRedhealId;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getAwardId() {
        return awardId;
    }

    public void setAwardId(String awardId) {
        this.awardId = awardId;
    }

    public String getAwardRedhealId() {
        return awardRedhealId;
    }

    public void setAwardRedhealId(String awardRedhealId) {
        this.awardRedhealId = awardRedhealId;
    }
}

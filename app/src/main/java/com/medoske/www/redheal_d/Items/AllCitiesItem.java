package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 14.7.17.
 */

public class AllCitiesItem {
    String citYId;
    String cityName;

    public AllCitiesItem(String citYId, String cityName) {
        this.citYId = citYId;
        this.cityName = cityName;
    }

    public String getCitYId() {
        return citYId;
    }

    public void setCitYId(String citYId) {
        this.citYId = citYId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}

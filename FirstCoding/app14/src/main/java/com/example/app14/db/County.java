package com.example.app14.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private String ccountyName;
    private String weatherId;
    private int cityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCcountyName(String ccountyName) {
        this.ccountyName = ccountyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public String getCcountyName() {
        return ccountyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return cityId;
    }
}

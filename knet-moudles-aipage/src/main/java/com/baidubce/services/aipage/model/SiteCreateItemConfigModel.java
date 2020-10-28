package com.baidubce.services.aipage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SiteCreateItemConfigModel {
    private String comboName = "site_miniprogram_2";
    private String name;
    private int count;
    private int time;

    public SiteCreateItemConfigModel(String name, int count, int time) {
        this.name = name;
        this.count = count;
        this.time = time;
    }

}

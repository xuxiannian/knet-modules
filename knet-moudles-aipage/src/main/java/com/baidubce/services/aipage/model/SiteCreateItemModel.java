package com.baidubce.services.aipage.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SiteCreateItemModel {
    private SiteCreateItemConfigModel config;
    private List<PaymentMethodModel> paymentMethod;

    public SiteCreateItemModel(String name, int count, int time) {
        this.config = new SiteCreateItemConfigModel(name, count, time);
    }

}

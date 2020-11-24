package com.baidubce.services.aipage.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SiteRenewItemModel {
    private SiteRenewItemConfigModel config;
    private List<PaymentMethodModel> paymentMethod;

    public SiteRenewItemModel(String siteId, String time) {
        this.config = new SiteRenewItemConfigModel(siteId,time);
    }

}

package com.baidubce.services.aipage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteModel {

    private String id;
    private String name;
    private Integer type;
    private String productSerie;
    private String productType;
    private String status;
    private Date createTime;
    private Date expireTime;
    private Boolean isOldData;
}

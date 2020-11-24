package com.baidubce.services.aipage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteListModel {
    Boolean isTruncated;
    String nextMarker;
    String orderId;
    List<SiteModel> sites;
}

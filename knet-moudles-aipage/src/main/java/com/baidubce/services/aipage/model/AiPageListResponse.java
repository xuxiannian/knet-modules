package com.baidubce.services.aipage.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AiPageListResponse extends AbstractAipageResponse {

    SiteListModel result;
}


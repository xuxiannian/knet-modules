package com.baidubce.services.aipage.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AiPageResponse extends AbstractAipageResponse {

    SiteModel result;
}


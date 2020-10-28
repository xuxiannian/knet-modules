package com.baidubce.services.aipage.model;

import com.baidubce.auth.BceCredentials;
import com.baidubce.model.AbstractBceRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AiPageCreateRequest extends AbstractBceRequest {


    List<SiteCreateItemModel> items;

    public AiPageCreateRequest(SiteCreateItemModel... items) {
        this.items = Arrays.stream(items).collect(Collectors.toList());
    }


    public AiPageCreateRequest withRequestCredentials(BceCredentials credentials) {
        this.setRequestCredentials(credentials);
        return this;
    }

}

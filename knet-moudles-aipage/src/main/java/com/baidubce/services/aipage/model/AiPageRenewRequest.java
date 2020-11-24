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
public class AiPageRenewRequest extends AbstractBceRequest {


    List<SiteRenewItemModel> items;

    public AiPageRenewRequest(SiteRenewItemModel... items) {
        this.items = Arrays.stream(items).collect(Collectors.toList());
    }


    public AiPageRenewRequest withRequestCredentials(BceCredentials credentials) {
        this.setRequestCredentials(credentials);
        return this;
    }

}

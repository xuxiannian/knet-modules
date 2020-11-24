package com.baidubce.services.aipage.model;

import lombok.Data;

@Data
public class AiPageRenewResponse extends AbstractAipageResponse {
    @Override
    public String toString() {
        return "AiPageRenewResponse{" +
                "result=" + result +
                ", success=" + success +
                ", status='" + status + '\'' +
                ", msg=" + msg +
                '}';
    }
}


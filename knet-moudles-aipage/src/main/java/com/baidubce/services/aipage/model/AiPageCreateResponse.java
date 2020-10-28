package com.baidubce.services.aipage.model;

import lombok.Data;

@Data
public class AiPageCreateResponse extends AbstractAipageResponse {
    @Override
    public String toString() {
        return "AiPageCreateResponse{" +
                "result=" + result +
                ", success=" + success +
                ", status='" + status + '\'' +
                ", msg=" + msg +
                '}';
    }
}


package com.baidubce.services.aipage.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AiPageResponse extends AbstractAipageResponse {
    @Override
    public String toString() {
        return "AiPageResponse{" +
                "result=" + result +
                ", success=" + success +
                ", status='" + status + '\'' +
                ", msg=" + msg +
                '}';
    }
}


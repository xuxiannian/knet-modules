package com.baidubce.services.aipage.model;

import com.baidubce.model.AbstractBceResponse;
import lombok.Data;

@Data
public class AbstractAipageResponse extends AbstractBceResponse {

    Object result;
    Boolean success;

    @Override
    public String toString() {
        return "AbstractAipageResponse{" +
                "result=" + result +
                ", success=" + success +
                '}';
    }
}

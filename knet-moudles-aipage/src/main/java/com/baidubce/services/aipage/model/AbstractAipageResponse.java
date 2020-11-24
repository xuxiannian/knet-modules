package com.baidubce.services.aipage.model;

import com.baidubce.model.AbstractBceResponse;
import lombok.Data;

@Data
public class AbstractAipageResponse extends AbstractBceResponse {

    Object result;
    Boolean success;
    Integer status;
    String msg;
    Object data;

    @Override
    public String toString() {
        return "AbstractAipageResponse{" +
                "result=" + result +
                ", success=" + success +
                ", status='" + status + '\'' +
                ", msg=" + msg +
                '}';
    }
}

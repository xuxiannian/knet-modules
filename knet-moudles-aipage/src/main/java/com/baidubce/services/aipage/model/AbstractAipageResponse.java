package com.baidubce.services.aipage.model;

import com.baidubce.model.AbstractBceResponse;
import lombok.Data;

import java.util.Map;

@Data
public class AbstractAipageResponse extends AbstractBceResponse {

    Map result;
    boolean success;
    String status;
    String msg;

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

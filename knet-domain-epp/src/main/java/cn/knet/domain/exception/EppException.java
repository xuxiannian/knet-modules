package cn.knet.domain.exception;

import cn.knet.domain.vo.DomainResult;

public class EppException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    private DomainResult domainResult;

    public EppException(DomainResult domainResult) {
        this.domainResult = domainResult;
    }


    public DomainResult getDomainResult() {
        return domainResult;
    }

    public void setDomainResult(DomainResult domainResult) {
        this.domainResult = domainResult;
    }
}

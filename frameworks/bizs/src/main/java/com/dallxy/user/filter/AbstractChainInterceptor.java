package com.dallxy.user.filter;

import lombok.Data;

@Data
public abstract class AbstractChainInterceptor<REQ> {

    private String type;

    abstract void preProcess(REQ request);

    abstract void postProcess(REQ request);

    abstract boolean handle(REQ request);

    boolean prehandle(REQ request) {
        preProcess(request);
        if (handle(request)) {
            postProcess(request);
            return true;
        }
        return false;
    }
}

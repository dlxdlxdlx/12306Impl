package com.dallxy.user.filter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class AbstractChainInterceptor<REQ> {

    private String type;

    void preProcess(REQ request){
        return;
    }

    void postProcess(REQ request){
        return;
    }

    public abstract boolean handle(REQ request);

    public boolean prehandle(REQ request) {
        preProcess(request);
        if (handle(request)) {
            postProcess(request);
            return true;
        }
        return false;
    }
}

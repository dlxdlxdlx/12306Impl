package com.dallxy.interceptors;

import com.dallxy.common.constant.UserInterceptorConstant;
import com.dallxy.dto.req.UserRegisterReqDTO;
import com.dallxy.service.UserService;
import com.dallxy.user.filter.AbstractChainInterceptor;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterHasUsernameInterceptor extends AbstractChainInterceptor<UserRegisterReqDTO> {

    private final UserService userService;

    public UserRegisterHasUsernameInterceptor(UserService userService){
        this.setType(UserInterceptorConstant.USER_REGISTER);
        this.userService = userService;
    }


    @Override
    public boolean handle(UserRegisterReqDTO request) {
        System.out.println("UserRegisterHasUsernameInterceptor checked");
        return userService.hasUsername(request.getUsername());
    }
}

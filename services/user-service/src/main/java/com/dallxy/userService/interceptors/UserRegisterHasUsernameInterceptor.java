package com.dallxy.userService.interceptors;

import com.dallxy.userService.common.constant.UserInterceptorConstant;
import com.dallxy.userService.dto.req.UserRegisterReqDTO;
import com.dallxy.userService.service.UserService;
import com.dallxy.user.filter.AbstractChainInterceptor;
import lombok.RequiredArgsConstructor;
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
        return userService.hasUsername(request.getUsername());
    }
}

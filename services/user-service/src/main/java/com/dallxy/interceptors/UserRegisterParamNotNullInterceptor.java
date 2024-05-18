package com.dallxy.interceptors;

import cn.hutool.core.bean.BeanUtil;
import com.dallxy.common.constant.UserInterceptorConstant;
import com.dallxy.dto.req.UserRegisterReqDTO;
import com.dallxy.user.filter.AbstractChainInterceptor;
import org.springframework.stereotype.Component;

/**
 * 检查是否存在空字段
 */
@Component
public class UserRegisterParamNotNullInterceptor extends AbstractChainInterceptor<UserRegisterReqDTO> {
    public UserRegisterParamNotNullInterceptor() {
        this.setType(UserInterceptorConstant.USER_REGISTER);
    }

    @Override
    public boolean handle(UserRegisterReqDTO request) {
        return !BeanUtil.hasNullField(request,
                "telephone", "address", "postCode", "userType", "verifyState", "region");
    }
}

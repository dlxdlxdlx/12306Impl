package com.dallxy.service.impl;

import com.dallxy.dto.*;
import com.dallxy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        return null;
    }

    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return null;
    }

    @Override
    public void logout(String accessToken) {

    }

    @Override
    public Boolean hasUsername(String username) {
        return null;
    }

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        return null;
    }

    @Override
    public void deletion(UserDeletionReqDTO requestParam) {

    }
}

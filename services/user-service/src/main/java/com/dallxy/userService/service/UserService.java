package com.dallxy.userService.service;

import com.dallxy.userService.dao.UserMailDao;
import com.dallxy.userService.dao.UserPhoneDao;
import com.dallxy.userService.dto.req.UserDeletionReqDTO;
import com.dallxy.userService.dto.req.UserLoginReqDTO;
import com.dallxy.userService.dto.req.UserRegisterReqDTO;
import com.dallxy.userService.dto.req.UserUpdateReqDTO;
import com.dallxy.userService.dto.resp.UserLoginRespDTO;
import com.dallxy.userService.dto.resp.UserQueryActualRespDTO;
import com.dallxy.userService.dto.resp.UserQueryRespDTO;
import com.dallxy.userService.dto.resp.UserRegisterRespDTO;
import com.dallxy.userService.dao.UserDao;
import jakarta.validation.constraints.NotEmpty;

public interface UserService {
    /**
     * 用户登录接口
     *
     * @param requestParam 用户登录入参
     * @return 用户登录返回结果
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 通过 Token 检查用户是否登录
     *
     * @param accessToken 用户登录 Token 凭证
     * @return 用户是否登录返回结果
     */
    UserLoginRespDTO checkLogin(String accessToken);

    /**
     * 用户退出登录
     *
     * @param accessToken 用户登录 Token 凭证
     */
    void logout(String accessToken);

    /**
     * 用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在返回结果
     */
    Boolean hasUsername(String username);

    /**
     * 用户注册
     *
     * @param requestParam 用户注册入参
     * @return 用户注册返回结果
     */
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);

    /**
     * 注销用户
     * 从 UserDao, UserPhoneDao, UserMailDao
     * 以及Local cache, Remote Cache中清除相关信息
     *
     * @param requestParam 注销用户入参
     * @see UserDao
     * @see UserPhoneDao
     * @see UserMailDao
     * @see com.dallxy.cache.ICache
     */
    void deletion(UserDeletionReqDTO requestParam);


    void update(UserUpdateReqDTO reqestParam);

    Integer queryUserDeletionNum(Integer idType, String idCard);

    UserQueryRespDTO queryUserByUsername(@NotEmpty String username);

    UserQueryRespDTO queryUserByUserId(@NotEmpty String userId);

    UserQueryActualRespDTO queryActualUserByUsername(@NotEmpty String username);

}

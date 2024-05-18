package com.dallxy.controller;

import com.dallxy.common.constant.UserInterceptorConstant;
import com.dallxy.common.result.Result;
import com.dallxy.common.result.Results;
import com.dallxy.dto.req.UserDeletionReqDTO;
import com.dallxy.dto.req.UserLoginReqDTO;
import com.dallxy.dto.req.UserRegisterReqDTO;
import com.dallxy.dto.req.UserUpdateReqDTO;
import com.dallxy.dto.resp.UserLoginRespDTO;
import com.dallxy.dto.resp.UserRegisterRespDTO;
import com.dallxy.service.UserService;
import com.dallxy.user.aop.annotation.VerifyArgs;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    //    private final UserInfoService infoService;

    /**
     * 用户登录
     */
    @PostMapping("/api/user-service/v1/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        return Results.success(userService.login(requestParam));
    }

    /**
     * 通过 Token 检查用户是否登录
     */
    @GetMapping("/api/user-service/check-login")
    public Result<UserLoginRespDTO> checkLogin(@RequestParam("accessToken") String accessToken) {
        UserLoginRespDTO result = userService.checkLogin(accessToken);
        return Results.success(result);
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/api/user-service/logout")
    public Result<Void> logout(@RequestParam(required = false) String accessToken) {
        userService.logout(accessToken);
        return Results.success();
    }


    /**
     * 根据用户名查询用户信息
     */
//    @GetMapping("/api/user-service/query")
//    public Result<UserQueryRespDTO> queryUserByUsername(@RequestParam("username") @NotEmpty String username) {
////        return Results.success(userService.queryUserByUsername(username));
//    }

    /**
     * 根据用户名查询用户无脱敏信息
     */
//    @GetMapping("/api/user-service/actual/query")
//    public Result<UserQueryActualRespDTO> queryActualUserByUsername(@RequestParam("username") @NotEmpty String username) {
////        return Results.success(infoService.queryActualUserByUsername(username));
//    }

    /**
     * 检查用户名是否已存在
     */
    @GetMapping("/api/user-service/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") @NotEmpty String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/user-service/register")
    @VerifyArgs(type = UserInterceptorConstant.USER_REGISTER, paramType = UserRegisterReqDTO.class)
    public Result<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO requestParam) {
        return Results.success(userService.register(requestParam));
    }

    /**
     * 修改用户
     */
    @PostMapping("/api/user-service/update")
    public Result<Void> update(@RequestBody @Valid UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 注销用户
     */
    @PostMapping("/api/user-service/deletion")
    public Result<Void> deletion(@RequestBody @Valid UserDeletionReqDTO requestParam) {
//        userLoginService.deletion(requestParam);
        userService.deletion(requestParam);
        return Results.success();
    }
}

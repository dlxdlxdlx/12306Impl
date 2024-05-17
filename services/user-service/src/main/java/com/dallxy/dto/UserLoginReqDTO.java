package com.dallxy.dto;

import lombok.Data;

@Data
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    private String usernameOrMailOrPhone;

    /**
     * 密码
     */
    private String password;
}

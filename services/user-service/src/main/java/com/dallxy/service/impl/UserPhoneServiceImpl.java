package com.dallxy.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dallxy.dao.UserPhoneDao;
import com.dallxy.mapper.UserPhoneMapper;
import com.dallxy.service.UserPhoneService;
import org.springframework.stereotype.Service;

/**
* @author LiXin
* @description 针对表【t_user_phone(用户号码表)】的数据库操作Service实现
* @createDate 2024-05-17 11:04:57
*/
@Service
public class UserPhoneServiceImpl extends ServiceImpl<UserPhoneMapper, UserPhoneDao>
    implements UserPhoneService {

}





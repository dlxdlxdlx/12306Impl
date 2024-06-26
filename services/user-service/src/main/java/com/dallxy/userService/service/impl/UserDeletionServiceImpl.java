package com.dallxy.userService.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.userService.dao.UserDeletionDao;
import com.dallxy.userService.service.UserDeletionService;
import com.dallxy.userService.mapper.UserDeletionMapper;
import org.springframework.stereotype.Service;


/**
* @author LiXin
* @description 针对表【t_user_deletion(用户注销表)】的数据库操作Service实现
* @createDate 2024-05-17 11:04:57
*/
@Service
public class UserDeletionServiceImpl extends ServiceImpl<UserDeletionMapper, UserDeletionDao>
    implements UserDeletionService {

}





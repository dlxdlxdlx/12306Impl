package com.dallxy.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dallxy.dao.UserMailDao;
import com.dallxy.service.UserMailService;
import com.dallxy.mapper.UserMailMapper;
import org.springframework.stereotype.Service;

/**
* @author LiXin
* @description 针对表【t_user_mail(用户邮箱表)】的数据库操作Service实现
* @createDate 2024-05-17 11:04:57
*/
@Service
public class UserMailServiceImpl extends ServiceImpl<UserMailMapper, UserMailDao>
    implements UserMailService {

}





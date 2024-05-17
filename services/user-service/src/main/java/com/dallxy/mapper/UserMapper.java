package com.dallxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.dao.UserDao;
import org.apache.ibatis.annotations.Mapper;

/**
* @author LiXin
* @description 针对表【t_user(用户表)】的数据库操作Mapper
* @createDate 2024-05-17 11:04:57
* @Entity com/dallxy/.dao.User
*/
@Mapper
public interface UserMapper extends BaseMapper<UserDao> {

}





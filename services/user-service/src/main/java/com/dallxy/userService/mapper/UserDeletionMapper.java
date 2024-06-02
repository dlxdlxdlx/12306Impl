package com.dallxy.userService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.userService.dao.UserDeletionDao;
import org.apache.ibatis.annotations.Mapper;

/**
* @author LiXin
* @description 针对表【t_user_deletion(用户注销表)】的数据库操作Mapper
* @createDate 2024-05-17 11:04:57
* @Entity com/dallxy/.dao.UserDeletion
*/
@Mapper
public interface UserDeletionMapper extends BaseMapper<UserDeletionDao> {

}





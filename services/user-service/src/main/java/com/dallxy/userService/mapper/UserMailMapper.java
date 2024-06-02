package com.dallxy.userService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.userService.dao.UserMailDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author LiXin
* @description 针对表【t_user_mail(用户邮箱表)】的数据库操作Mapper
* @createDate 2024-05-17 11:04:57
* @Entity com/dallxy/.dao.UserMail
*/
@Mapper
public interface UserMailMapper extends BaseMapper<UserMailDao> {
    @Select("select username from t_user where mail = #{mail}")
    String getUserNameByMail(@Param("mail") String mail);

    @Select("select username from t_user where mail = #{phone}")
    String getUserNameByPhone(@Param("phone") String phone);

    @Update("update t_user_mail set deletion_time=#{deletionTime} , del_flag='1' where username = #{username} and del_flag='0'")
    void deletionUser(UserMailDao userMailDao);
}





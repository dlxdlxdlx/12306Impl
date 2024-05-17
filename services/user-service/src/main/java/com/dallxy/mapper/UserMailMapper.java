package com.dallxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.dao.UserMailDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}





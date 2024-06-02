package com.dallxy.userService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.userService.dao.PassengerDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PassengerMapper extends BaseMapper<PassengerDao> {
    @Select("select * from t_passenger where username = #{username} and id = #{id}")
    PassengerDao selectPassengerByUsernameAndId(@Param("username") String username, @Param("id") Long id);
}
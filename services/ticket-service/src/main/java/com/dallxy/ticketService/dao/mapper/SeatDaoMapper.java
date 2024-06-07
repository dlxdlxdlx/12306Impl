package com.dallxy.ticketService.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dallxy.ticketService.dao.SeatDao;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeatDaoMapper extends BaseMapper<SeatDao> {

    @Data
    class SeatRemain{
        public Integer type;
        public Integer margin;
    }

    @Select("select seat_type as type, count(*) as margin from t_seat where train_id=#{trainId}  and seat_status=#{status} and start_station=#{startStation} and end_station=#{endStation} group by seat_type")
    List<SeatRemain> listSeatRemain(@Param("trainId")String trainId,
//                                    @Param("type") Integer type,
                                    @Param("status")Integer seatStatus,
                                    @Param("startStation")String startStation,
                                    @Param("endStation")String endStation);
}
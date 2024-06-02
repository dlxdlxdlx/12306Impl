package com.dallxy.ticketService.interceptors;

import cn.hutool.core.bean.BeanUtil;
import com.dallxy.ticketService.dto.req.TicketPageQueryReqDTO;
import com.dallxy.user.filter.AbstractChainInterceptor;

public class TrainTicketParamNotNullInterceptor extends AbstractChainInterceptor<TicketPageQueryReqDTO> {
    @Override
    public boolean handle(TicketPageQueryReqDTO request) {
        return !BeanUtil.hasNullField(
                request,
                "arrival", "departure"
        );
    }
}

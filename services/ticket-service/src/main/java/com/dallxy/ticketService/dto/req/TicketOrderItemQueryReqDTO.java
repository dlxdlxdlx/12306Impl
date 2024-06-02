package com.dallxy.ticketService.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class TicketOrderItemQueryReqDTO {
    private String orderSn;

    private List<String> orderItemRecordIds;
}

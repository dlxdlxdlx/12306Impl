package com.dallxy.ticketService.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CancelTicketOrderReqDTO {
    private String orderSn;
}

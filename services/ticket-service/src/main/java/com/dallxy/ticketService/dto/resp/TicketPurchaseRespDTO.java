package com.dallxy.ticketService.dto.resp;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class TicketPurchaseRespDTO {
    private String orderSn;

    private List<TicketOrderDetailRespDTO> ticketOrderDetails;
}

package com.dallxy.ticketService.dto.resp;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class TicketOrderDetailRespDTO {
    private Integer seatType;

    private String carriageNumber;

    private String seatNumber;

    private String idType;

    private String idCard;

    private String ticketType;

    private Integer amount;
}

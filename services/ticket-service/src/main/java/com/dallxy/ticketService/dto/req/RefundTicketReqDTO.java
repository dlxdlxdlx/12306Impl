package com.dallxy.ticketService.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class RefundTicketReqDTO {
    private String orderSn;

    private Integer type;


    private List<String> subOrderRecordIdReqList;
}

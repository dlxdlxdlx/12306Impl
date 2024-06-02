package com.dallxy.ticketService.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TicketPageQueryReqDTO {
    private String formStation;
    private String toStation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;

    private String departure;

    private String arrival;
}

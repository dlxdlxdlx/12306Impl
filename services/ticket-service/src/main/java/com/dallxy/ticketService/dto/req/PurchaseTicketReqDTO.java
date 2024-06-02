package com.dallxy.ticketService.dto.req;

import com.dallxy.ticketService.dto.domain.PurchaseTicketPassengerDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PurchaseTicketReqDTO {
    private String trainId;

    private List<PurchaseTicketPassengerDetailDTO> passengers;


    private List<String> chooseSeats;

    private String departure;


    private String arrival;

}

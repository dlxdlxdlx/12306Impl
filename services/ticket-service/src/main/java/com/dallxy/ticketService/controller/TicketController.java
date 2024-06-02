package com.dallxy.ticketService.controller;

import com.dallxy.common.result.Result;
import com.dallxy.ticketService.dto.req.CancelTicketOrderReqDTO;
import com.dallxy.ticketService.dto.req.PurchaseTicketReqDTO;
import com.dallxy.ticketService.dto.req.RefundTicketReqDTO;
import com.dallxy.ticketService.dto.req.TicketPageQueryReqDTO;
import com.dallxy.ticketService.dto.resp.RefundTicketRespDTO;
import com.dallxy.ticketService.dto.resp.TicketPageQueryRespDTO;
import com.dallxy.ticketService.dto.resp.TicketPurchaseRespDTO;
import com.dallxy.ticketService.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    @GetMapping("/api/ticket-service/ticket/query")
    public Result<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO requestParam){
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/api/ticket-service/ticket/purchase")
    public Result<TicketPurchaseRespDTO> purchaseTickets(@RequestBody PurchaseTicketReqDTO requestParam){
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/api/ticket-service/ticket/purchase/v2")
    public Result<TicketPurchaseRespDTO> purchaseTicketV2(@RequestBody PurchaseTicketReqDTO requestParam){
        throw new UnsupportedOperationException("Not implemented");
    }


    @PostMapping("/api/ticket-service/ticket/cancel")
    public Result<Void> cancelTicket(@RequestBody CancelTicketOrderReqDTO requestParam){
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/api/ticket-service/ticket/refund")
    public Result<RefundTicketRespDTO> commonTicketRefund(@RequestBody RefundTicketReqDTO requestParam){
        throw new UnsupportedOperationException("Not implemented");
    }
}

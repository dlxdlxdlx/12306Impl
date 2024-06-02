package com.dallxy.orderService.controller;

import com.dallxy.common.result.Result;
import com.dallxy.common.result.Results;
import com.dallxy.orderService.dto.req.TicketOrderItemQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketOrderController {

    /** 根据订单号查询车票订单
     * @param orderSn 订单号
     * @return
     */
    @GetMapping("/api/order-service/order/ticket/query")
    public Result<TicketOrderDetailRespDTO> queryTicketOrderByOrderSn(@RequestParam("orderSn")String orderSn){
        return Results.success();
    }

    /** 根据子订单Id查询车票订单详情
     * @param requestParam 请求参数
     * @return
     */
    @GetMapping("/api/order-service/order/item/ticket/query")
    public Result<List<TicketOrderPassengerDetailRespDTO>> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam){
        return Results.success();
    }

    /** 分页查询车票订单
     * @param requestParam 请求参数
     * @return
     */
    @GetMapping("/api/order-service/order/ticket/page")
    public Result<PageResponse<TicketOrderDetailRespDTO>> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam){
        return Results.success();
    }



    /**车票取消
     *
     * @param requestParam
     * @return
     */
    @PostMapping("/api/order-service/order/ticket/cancel")
    public Result<Boolean> cancelTicketOrder(@RequestBody CancelTicketOrderReqDTO requestParam){
        return Results.success();
    }

}

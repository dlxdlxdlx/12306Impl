package com.dallxy.orderService.controller;

import com.dallxy.common.result.Result;
import com.dallxy.common.result.Results;
import com.dallxy.database.dao.PageResponse;
import com.dallxy.orderService.dto.req.*;
import com.dallxy.orderService.dto.resp.TicketOrderDetailRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailSelfRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.dallxy.orderService.service.OrderItemService;
import com.dallxy.orderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketOrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    /**
     * 根据订单号查询车票订单
     *
     * @param orderSn 订单号
     * @return
     */
    @GetMapping("/api/order-service/order/ticket/query")
    public Result<TicketOrderDetailRespDTO> queryTicketOrderByOrderSn(@RequestParam("orderSn") String orderSn) {
        return Results.success(orderService.queryTicketOrderByOrderSn(orderSn));
    }

    /**
     * 根据子订单Id查询车票订单详情
     *
     * @param requestParam 请求参数
     * @return
     */
    @GetMapping("/api/order-service/order/item/ticket/query")
    public Result<List<TicketOrderPassengerDetailRespDTO>> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam) {
        return Results.success(orderItemService.queryTicketItemOrderById(requestParam));
    }

    /**
     * 分页查询车票订单
     *
     * @param requestParam 请求参数
     * @return
     */
    @GetMapping("/api/order-service/order/ticket/page")
    public Result<PageResponse<TicketOrderDetailRespDTO>> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        return Results.success(orderService.pageTicketOrder(requestParam));
    }

    /**
     * 分页查询本人车票订单
     *
     * @param requestParam 请求参数
     * @return
     */
    @GetMapping("/api/order-service/order/ticket/self/page")
    public Result<PageResponse<TicketOrderDetailSelfRespDTO>> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam) {
        return Results.success(orderService.pageSelfTicketOrder(requestParam));
    }


    /**
     * 车票订单创建
     *
     * @param requestParam 请求参数
     * @return
     */
    @PostMapping("/api/order-service/order/ticket/create")
    public Result<String> createTicketOrder(@RequestBody TicketOrderCreateReqDTO requestParam) {
        return Results.success(orderService.createTicketOrder(requestParam));
    }

    /**
     * 车票关闭订单
     *
     * @param requestParam 请求参数
     * @return
     */
    @PostMapping("/api/order-service/order/ticket/close")
    public Result<Boolean> closeTicketOrder(@RequestBody CancelTicketOrderReqDTO requestParam) {
        return Results.success(orderService.closeTicketOrder(requestParam));
    }

    /**
     * 车票取消
     *
     * @param requestParam 请求参数
     * @return
     */
    @PostMapping("/api/order-service/order/ticket/cancel")
    public Result<Boolean> cancelTicketOrder(@RequestBody CancelTicketOrderReqDTO requestParam) {
        return Results.success(orderService.cancelTicketOrder(requestParam));
    }

}

package com.dallxy.orderService.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dallxy.database.dao.PageResponse;
import com.dallxy.orderService.dao.entity.OrderDao;
import com.dallxy.orderService.dto.domain.OrderStatusReversalDTO;
import com.dallxy.orderService.dto.req.CancelTicketOrderReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderCreateReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderPageQueryReqDTO;
import com.dallxy.orderService.dto.req.TicketOrderSelfPageQueryReqDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailSelfRespDTO;

/**
 * 订单接口层
 */
public interface OrderService extends IService<OrderDao> {
    /** 根据订单号查询车票订单
     * @param orderSn 请求参数
     * @return 订单详情
     */
    TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn);


    /** 根据用户名分页查询车票订单
     * @param requestParam
     * @return
     */
    PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam);

    /** 创建火车票订单
     * @param requestParam
     * @return
     */
    String createTicketOrder(TicketOrderCreateReqDTO requestParam);


    /** 取消火车票订单
     * @param requestParam 请求参数
     * @return
     */
    boolean closeTicketOrder(CancelTicketOrderReqDTO requestParam);

    /** 取消火车票
     * @param requestParam 请求参数
     * @return
     */
    boolean cancelTicketOrder(CancelTicketOrderReqDTO requestParam);

    /**订单状态翻转
     * @param requestParam 请求参数
     */
    void orderStatusReversal(OrderStatusReversalDTO requestParam);


    /** 查询本人车票订单
     * @param requestParam 请求参数
     * @return
     */
    PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam);

}

package com.dallxy.ticketService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dallxy.ticketService.dao.TicketDao;
import com.dallxy.ticketService.dto.req.CancelTicketOrderReqDTO;
import com.dallxy.ticketService.dto.req.PurchaseTicketReqDTO;
import com.dallxy.ticketService.dto.req.RefundTicketReqDTO;
import com.dallxy.ticketService.dto.req.TicketPageQueryReqDTO;
import com.dallxy.ticketService.dto.resp.PayInfoRespDTO;
import com.dallxy.ticketService.dto.resp.RefundTicketRespDTO;
import com.dallxy.ticketService.dto.resp.TicketPageQueryRespDTO;
import com.dallxy.ticketService.dto.resp.TicketPurchaseRespDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface TicketService extends IService<TicketDao> {
    /**
     * 根据条件分页查询车票
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam);

    /**
     * 根据条件分页查询车票V2高性能版本
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    TicketPageQueryRespDTO pageListTicketQueryV2(TicketPageQueryReqDTO requestParam);

    /**
     * 购买车票
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号
     */
    TicketPurchaseRespDTO purchaseTicketsV1(@RequestBody PurchaseTicketReqDTO requestParam);

    /**
     * 购买车票V2高性能版本
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号
     */
    TicketPurchaseRespDTO purchaseTicketsV2(@RequestBody PurchaseTicketReqDTO requestParam);

    /**
     * 执行购买车票
     * 被对应购票版本号接口调用 {@link TicketService#purchaseTicketsV1(PurchaseTicketReqDTO)} and {@link TicketService#purchaseTicketsV2(PurchaseTicketReqDTO)}
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号
     */
    TicketPurchaseRespDTO executePurchaseTickets(@RequestBody PurchaseTicketReqDTO requestParam);

    /**
     * 支付单详情查询
     *
     * @param orderSn 订单号
     * @return 支付单详情
     */
    PayInfoRespDTO getPayInfo(String orderSn);

    /**
     * 取消车票订单
     *
     * @param requestParam 取消车票订单入参
     */
    void cancelTicketOrder(CancelTicketOrderReqDTO requestParam);

    /**
     * 公共退款接口
     *
     * @param requestParam 退款请求参数
     * @return 退款返回详情
     */
    RefundTicketRespDTO commonTicketRefund(RefundTicketReqDTO requestParam);
}

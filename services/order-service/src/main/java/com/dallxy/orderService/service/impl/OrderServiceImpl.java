package com.dallxy.orderService.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dallxy.common.exception.ClientException;
import com.dallxy.common.exception.ServiceException;
import com.dallxy.common.result.Result;
import com.dallxy.database.dao.PageResponse;
import com.dallxy.orderService.common.constant.OrderMQConstant;
import com.dallxy.orderService.common.enums.OrderCancelErrorCodeEnum;
import com.dallxy.orderService.common.enums.OrderStatusEnum;
import com.dallxy.orderService.dao.entity.OrderDao;
import com.dallxy.orderService.dao.entity.OrderItemDao;
import com.dallxy.orderService.dao.entity.OrderItemPassengerDao;
import com.dallxy.orderService.dao.mapper.OrderItemMapper;
import com.dallxy.orderService.dao.mapper.OrderItemPassengerMapper;
import com.dallxy.orderService.dao.mapper.OrderMapper;
import com.dallxy.orderService.dto.domain.OrderStatusReversalDTO;
import com.dallxy.orderService.dto.req.*;
import com.dallxy.orderService.dto.resp.TicketOrderDetailRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderDetailSelfRespDTO;
import com.dallxy.orderService.dto.resp.TicketOrderPassengerDetailRespDTO;
import com.dallxy.orderService.remote.UserRemoteService;
import com.dallxy.orderService.remote.dto.UserQueryActualRespDTO;
import com.dallxy.orderService.service.OrderItemService;
import com.dallxy.orderService.service.OrderPassengerRelationService;
import com.dallxy.orderService.service.OrderService;
import com.dallxy.user.core.UserContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper,OrderDao> implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final RedissonClient redissonClient;
    private final UserRemoteService userRemoteService;
    private final OrderItemPassengerMapper orderItemPassengerMapper;
    private final OrderItemService orderItemService;
    private final OrderPassengerRelationService orderPassengerRelationService;


    /**
     * 根据订单号查询车票订单
     * 单次订单可能会涉及多个乘客,所以需要设置对应的乘客名单
     * @param orderSn 请求参数
     * @return 订单详情
     */
    @Override
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn) {
        OrderDao orderEntity = orderMapper.selectOne(Wrappers.lambdaQuery(OrderDao.class).eq(OrderDao::getOrderSn, orderSn));
        TicketOrderDetailRespDTO result = BeanUtil.copyProperties(orderEntity, TicketOrderDetailRespDTO.class);
        List<OrderItemDao> orderItemDaoList = orderItemMapper.selectList(Wrappers.lambdaQuery(OrderItemDao.class).eq(OrderItemDao::getOrderSn, orderSn));
        List<TicketOrderPassengerDetailRespDTO> passengerDetails = orderItemDaoList.stream()
                .map(e -> BeanUtil.copyProperties(e, TicketOrderPassengerDetailRespDTO.class))
                .collect(Collectors.toList());
        result.setPassengerDetails(passengerDetails);
        return result;
    }

    /**
     * 根据userId 以及订单状态 @{@link TicketOrderPageQueryReqDTO} 分页查询车票订单
     *
     * @return
     */
    @Override
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        Page<OrderDao> orderPage = orderMapper.selectPage(new Page<OrderDao>(requestParam.getCurrent(), requestParam.getSize()),
                Wrappers.lambdaQuery(OrderDao.class)
                        .eq(OrderDao::getUserId, requestParam.getUserId())
                        .in(OrderDao::getStatus, requestParam.getStatusType())
                        .orderByDesc(OrderDao::getOrderTime));
        return PageResponse.convert(orderPage, e -> {
            TicketOrderDetailRespDTO ticketOrderDetailRespDTO = BeanUtil.copyProperties(e, TicketOrderDetailRespDTO.class);
            LambdaQueryWrapper<OrderItemDao> queryWrapper = Wrappers.lambdaQuery(OrderItemDao.class)
                    .eq(OrderItemDao::getOrderSn, e.getOrderSn());
            List<TicketOrderPassengerDetailRespDTO> list = orderItemMapper.selectList(queryWrapper).stream()
                    .map(p -> BeanUtil.copyProperties(p, TicketOrderPassengerDetailRespDTO.class))
                    .collect(Collectors.toList());
            ticketOrderDetailRespDTO.setPassengerDetails(list);

            return ticketOrderDetailRespDTO;
        });
    }

    /**
     * 创建火车票订单
     *
     * @param requestParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam) {
        String orderSn = "";
//        TODO: 思考通过什么方式生成ID
        Assert.notNull(orderSn);

        OrderDao orderDao = new OrderDao();
        orderDao.setOrderSn(orderSn);
        orderDao.setStatus(OrderStatusEnum.PENDING_PAYMENT.getStatus());
        BeanUtil.copyProperties(requestParam, orderDao);

        int inserted = orderMapper.insert(orderDao);
        if(!SqlHelper.retBool(inserted)){
           throw new ServiceException("创建订单失败");
        }
        List<TicketOrderItemCreateReqDTO> ticketOrderItems = requestParam.getTicketOrderItems();
        ArrayList<OrderItemDao> orderItemDaoList = new ArrayList<>();
        ArrayList<OrderItemPassengerDao> orderItemPassengerDaoList = new ArrayList<OrderItemPassengerDao>();
        ticketOrderItems.forEach(
                e->{
                    OrderItemDao orderItemDao = new OrderItemDao();
                    BeanUtil.copyProperties(e,orderItemDao);
                    orderItemDao.setTrainId(requestParam.getTrainId());
                    orderItemDao.setUsername(requestParam.getUsername());
                    orderItemDao.setUserId(requestParam.getUserId());
                    orderItemDao.setStatus(0);
                    orderItemDaoList.add(orderItemDao);
                    OrderItemPassengerDao orderItemPassengerDao = BeanUtil.copyProperties(e, OrderItemPassengerDao.class);
                    orderItemPassengerDao.setOrderSn(orderSn);
                    orderItemPassengerDaoList.add(orderItemPassengerDao);
                }
        );
        orderItemService.saveBatch(orderItemDaoList);
        orderPassengerRelationService.saveBatch(orderItemPassengerDaoList);
//        TODO: 发送RocketMQ延时消息, 指定时间取消订单
        return orderSn;
    }

    /**
     * 取消火车票订单
     *
     * @param requestParam 请求参数
     * @return
     */
    @Override
    public boolean closeTicketOrder(CancelTicketOrderReqDTO requestParam) {
        String orderSn = requestParam.getOrderSn();
        LambdaQueryWrapper<OrderDao> queryWrapper = Wrappers.lambdaQuery(OrderDao.class)
                .eq(OrderDao::getOrderSn, orderSn)
                .select(OrderDao::getStatus);
        OrderDao orderDao = orderMapper.selectOne(queryWrapper);
        if(Objects.isNull(orderDao)|| orderDao.getStatus()!=OrderStatusEnum.PENDING_PAYMENT.getStatus()){
            return false;
        }
        return cancelTicketOrder(requestParam);
    }

    /**
     * 取消火车票订单,将订单的状态设置为CLOSED
     *
     * @param requestParam 请求参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTicketOrder(CancelTicketOrderReqDTO requestParam) {
        OrderDao orderDao = orderMapper.selectOne(Wrappers.lambdaQuery(OrderDao.class)
                .eq(OrderDao::getOrderSn, requestParam.getOrderSn()));
        if (Objects.isNull(orderDao)) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_UNKNOWN_ERROR.message());
        } else if (orderDao.getStatus() != OrderStatusEnum.PENDING_PAYMENT.getStatus()) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_STATUS_ERROR.message());
        }
        RLock lock = redissonClient.getLock(OrderMQConstant.LOCK_ORDER_CANCEL_SN_PREFIX + requestParam.getOrderSn());
        if (!lock.tryLock())
            throw new ClientException(OrderCancelErrorCodeEnum.ORDER_CANCEL_REPETITION_ERROR.message());
        try {
            OrderDao updateOrderDao = new OrderDao();
            updateOrderDao.setStatus(OrderStatusEnum.CLOSED.getStatus());
            int update = orderMapper.update(updateOrderDao, Wrappers.lambdaUpdate(OrderDao.class)
                    .eq(OrderDao::getOrderSn, requestParam.getOrderSn()));
            if (!SqlHelper.retBool(update))
                throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_ERROR.message());
        } finally {
            lock.unlock();
        }

        return true;
    }

    /**
     * 订单状态翻转
     * Question: 订单状态翻转和关闭订单之间的关系是什么
     *
     * @param requestParam 请求参数
     */
    @Override
    public void orderStatusReversal(OrderStatusReversalDTO requestParam) {
        OrderDao orderDao = orderMapper.selectOne(Wrappers.lambdaQuery(OrderDao.class)
                .eq(OrderDao::getOrderSn, requestParam.getOrderSn()));
        if (orderDao == null) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_CANCEL_UNKNOWN_ERROR.message());
        } else if (orderDao.getStatus() != OrderStatusEnum.PENDING_PAYMENT.getStatus()) {
            throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_STATUS_REVERSAL_ERROR.message());
        }

        RLock lock = redissonClient.getLock(OrderMQConstant.LOCK_ORDER_CANCEL_SN_PREFIX + requestParam.getOrderSn());
        if (!lock.tryLock()) {
            throw new ClientException(String.format("订单重复修改状态, %s", JSON.toJSONString(requestParam)));
        }
        try {
            OrderItemDao orderItemDao = new OrderItemDao();
            orderItemDao.setStatus(requestParam.getOrderItemStatus());
            int update = orderItemMapper.update(orderItemDao, Wrappers.lambdaUpdate(OrderItemDao.class)
                    .eq(OrderItemDao::getOrderSn, requestParam.getOrderSn()));
            if (!SqlHelper.retBool(update)) {
                throw new ServiceException(OrderCancelErrorCodeEnum.ORDER_STATUS_REVERSAL_ERROR.message());
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 查询本人车票订单
     *
     * @param requestParam 请求参数
     */
    @Override
    public PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam) {
        Result<UserQueryActualRespDTO> userActualResp = userRemoteService.queryActualUserByUsername(UserContext.getUsername());
        LambdaQueryWrapper<OrderItemPassengerDao> queryWrapper = Wrappers.lambdaQuery(OrderItemPassengerDao.class)
                .eq(OrderItemPassengerDao::getIdCard, userActualResp.getData().getIdCard())
                .orderByDesc(OrderItemPassengerDao::getCreateTime);
        Page<OrderItemPassengerDao> page = new Page<>();
        page.setCurrent(requestParam.getCurrent());
        page.setSize(requestParam.getSize());
        Page<OrderItemPassengerDao> orderItemPassengerPage = orderItemPassengerMapper.selectPage(page, queryWrapper);
        return PageResponse.convert(orderItemPassengerPage, e -> {
            OrderDao orderDao = orderMapper.selectOne(Wrappers.lambdaQuery(OrderDao.class)
                    .eq(OrderDao::getOrderSn, e.getOrderSn()));
            OrderItemDao orderItemDao = orderItemMapper.selectOne(Wrappers.lambdaQuery(OrderItemDao.class)
                    .eq(OrderItemDao::getOrderSn, e.getOrderSn())
                    .eq(OrderItemDao::getIdCard, e.getIdCard()));
            TicketOrderDetailSelfRespDTO actualResults = BeanUtil.copyProperties(orderDao, TicketOrderDetailSelfRespDTO.class);
            CopyOptions copyOptions = new CopyOptions();
            copyOptions.setIgnoreNullValue(true);
            BeanUtil.copyProperties(orderItemDao, actualResults, copyOptions);
            return actualResults;
        });
    }



}

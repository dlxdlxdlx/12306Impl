package com.dallxy.orderService.dto.domain;

import com.dallxy.orderService.dao.entity.OrderItemDao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 子订单状态反转实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemStatusReversalDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 订单反转后状态
     */
    private Integer orderStatus;

    /**
     * 订单明细反转后状态
     */
    private Integer orderItemStatus;

    /**
     * 订单明细集合
     */
    private List<OrderItemDao> orderItemDOList;
}

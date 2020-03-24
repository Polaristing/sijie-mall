package com.sijie.mall.dao;

import com.sijie.mall.entity.SiJieMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SiJieMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(SiJieMallOrderItem record);

    int insertSelective(SiJieMallOrderItem record);

    SiJieMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<SiJieMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<SiJieMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<SiJieMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(SiJieMallOrderItem record);

    int updateByPrimaryKey(SiJieMallOrderItem record);
}
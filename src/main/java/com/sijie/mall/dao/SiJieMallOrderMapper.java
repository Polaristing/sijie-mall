package com.sijie.mall.dao;

import com.sijie.mall.entity.SiJieMallOrder;
import com.sijie.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SiJieMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(SiJieMallOrder record);

    int insertSelective(SiJieMallOrder record);

    SiJieMallOrder selectByPrimaryKey(Long orderId);

    SiJieMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(SiJieMallOrder record);

    int updateByPrimaryKey(SiJieMallOrder record);

    List<SiJieMallOrder> findSiJieMallOrderList(PageQueryUtil pageUtil);

    int getTotalSiJieMallOrders(PageQueryUtil pageUtil);

    List<SiJieMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}
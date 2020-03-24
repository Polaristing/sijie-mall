package com.sijie.mall.service.impl;

import com.sijie.mall.common.*;
import com.sijie.mall.controller.vo.*;
import com.sijie.mall.dao.SiJieMallGoodsMapper;
import com.sijie.mall.dao.SiJieMallOrderItemMapper;
import com.sijie.mall.dao.SiJieMallOrderMapper;
import com.sijie.mall.dao.SiJieMallShoppingCartItemMapper;
import com.sijie.mall.entity.SiJieMallGoods;
import com.sijie.mall.entity.SiJieMallOrder;
import com.sijie.mall.entity.SiJieMallOrderItem;
import com.sijie.mall.entity.StockNumDTO;
import com.sijie.mall.service.SiJieMallOrderService;
import com.sijie.mall.util.BeanUtil;
import com.sijie.mall.util.NumberUtil;
import com.sijie.mall.util.PageQueryUtil;
import com.sijie.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class SiJieMallOrderServiceImpl implements SiJieMallOrderService {

    @Autowired
    private SiJieMallOrderMapper siJieMallOrderMapper;
    @Autowired
    private SiJieMallOrderItemMapper siJieMallOrderItemMapper;
    @Autowired
    private SiJieMallShoppingCartItemMapper siJieMallShoppingCartItemMapper;
    @Autowired
    private SiJieMallGoodsMapper siJieMallGoodsMapper;

    @Override
    public PageResult getSiJieMallOrdersPage(PageQueryUtil pageUtil) {
        List<SiJieMallOrder> siJieMallOrders = siJieMallOrderMapper.findSiJieMallOrderList(pageUtil);
        int total = siJieMallOrderMapper.getTotalSiJieMallOrders(pageUtil);
        PageResult pageResult = new PageResult(siJieMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(SiJieMallOrder siJieMallOrder) {
        SiJieMallOrder temp = siJieMallOrderMapper.selectByPrimaryKey(siJieMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(siJieMallOrder.getTotalPrice());
            temp.setUserAddress(siJieMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (siJieMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SiJieMallOrder> orders = siJieMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SiJieMallOrder siJieMallOrder : orders) {
                if (siJieMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += siJieMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (siJieMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += siJieMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (siJieMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SiJieMallOrder> orders = siJieMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SiJieMallOrder siJieMallOrder : orders) {
                if (siJieMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += siJieMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (siJieMallOrder.getOrderStatus() != 1 && siJieMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += siJieMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (siJieMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<SiJieMallOrder> orders = siJieMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (SiJieMallOrder siJieMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (siJieMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += siJieMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (siJieMallOrder.getOrderStatus() == 4 || siJieMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += siJieMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (siJieMallOrderMapper.closeOrder(Arrays.asList(ids), SiJieMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(SiJieMallUserVO user, List<SiJieMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(SiJieMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(SiJieMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<SiJieMallGoods> siJieMallGoods = siJieMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, SiJieMallGoods> siJieMallGoodsMap = siJieMallGoods.stream().collect(Collectors.toMap(SiJieMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (SiJieMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!siJieMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                SiJieMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > siJieMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                SiJieMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(siJieMallGoods)) {
            if (siJieMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = siJieMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    SiJieMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                SiJieMallOrder siJieMallOrder = new SiJieMallOrder();
                siJieMallOrder.setOrderNo(orderNo);
                siJieMallOrder.setUserId(user.getUserId());
                siJieMallOrder.setUserAddress(user.getAddress());
                //总价
                for (SiJieMallShoppingCartItemVO siJieMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += siJieMallShoppingCartItemVO.getGoodsCount() * siJieMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    SiJieMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                siJieMallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                siJieMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (siJieMallOrderMapper.insertSelective(siJieMallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<SiJieMallOrderItem> siJieMallOrderItems = new ArrayList<>();
                    for (SiJieMallShoppingCartItemVO siJieMallShoppingCartItemVO : myShoppingCartItems) {
                        SiJieMallOrderItem siJieMallOrderItem = new SiJieMallOrderItem();
                        //使用BeanUtil工具类将siJieMallShoppingCartItemVO中的属性复制到siJieMallOrderItem对象中
                        BeanUtil.copyProperties(siJieMallShoppingCartItemVO, siJieMallOrderItem);
                        //SiJieMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        siJieMallOrderItem.setOrderId(siJieMallOrder.getOrderId());
                        siJieMallOrderItems.add(siJieMallOrderItem);
                    }
                    //保存至数据库
                    if (siJieMallOrderItemMapper.insertBatch(siJieMallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    SiJieMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                SiJieMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            SiJieMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        SiJieMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public SiJieMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        SiJieMallOrder siJieMallOrder = siJieMallOrderMapper.selectByOrderNo(orderNo);
        if (siJieMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            List<SiJieMallOrderItem> orderItems = siJieMallOrderItemMapper.selectByOrderId(siJieMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<SiJieMallOrderItemVO> siJieMallOrderItemVOS = BeanUtil.copyList(orderItems, SiJieMallOrderItemVO.class);
                SiJieMallOrderDetailVO siJieMallOrderDetailVO = new SiJieMallOrderDetailVO();
                BeanUtil.copyProperties(siJieMallOrder, siJieMallOrderDetailVO);
                siJieMallOrderDetailVO.setOrderStatusString(SiJieMallOrderStatusEnum.getSiJieMallOrderStatusEnumByStatus(siJieMallOrderDetailVO.getOrderStatus()).getName());
                siJieMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(siJieMallOrderDetailVO.getPayType()).getName());
                siJieMallOrderDetailVO.setSiJieMallOrderItemVOS(siJieMallOrderItemVOS);
                return siJieMallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public SiJieMallOrder getSiJieMallOrderByOrderNo(String orderNo) {
        return siJieMallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = siJieMallOrderMapper.getTotalSiJieMallOrders(pageUtil);
        List<SiJieMallOrder> siJieMallOrders = siJieMallOrderMapper.findSiJieMallOrderList(pageUtil);
        List<SiJieMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(siJieMallOrders, SiJieMallOrderListVO.class);
            //设置订单状态中文显示值
            for (SiJieMallOrderListVO siJieMallOrderListVO : orderListVOS) {
                siJieMallOrderListVO.setOrderStatusString(SiJieMallOrderStatusEnum.getSiJieMallOrderStatusEnumByStatus(siJieMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = siJieMallOrders.stream().map(SiJieMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<SiJieMallOrderItem> orderItems = siJieMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<SiJieMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(SiJieMallOrderItem::getOrderId));
                for (SiJieMallOrderListVO siJieMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(siJieMallOrderListVO.getOrderId())) {
                        List<SiJieMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(siJieMallOrderListVO.getOrderId());
                        //将SiJieMallOrderItem对象列表转换成SiJieMallOrderItemVO对象列表
                        List<SiJieMallOrderItemVO> siJieMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, SiJieMallOrderItemVO.class);
                        siJieMallOrderListVO.setSiJieMallOrderItemVOS(siJieMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        SiJieMallOrder siJieMallOrder = siJieMallOrderMapper.selectByOrderNo(orderNo);
        if (siJieMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (siJieMallOrderMapper.closeOrder(Collections.singletonList(siJieMallOrder.getOrderId()), SiJieMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        SiJieMallOrder siJieMallOrder = siJieMallOrderMapper.selectByOrderNo(orderNo);
        if (siJieMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            siJieMallOrder.setOrderStatus((byte) SiJieMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            siJieMallOrder.setUpdateTime(new Date());
            if (siJieMallOrderMapper.updateByPrimaryKeySelective(siJieMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        SiJieMallOrder siJieMallOrder = siJieMallOrderMapper.selectByOrderNo(orderNo);
        if (siJieMallOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            siJieMallOrder.setOrderStatus((byte) SiJieMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            siJieMallOrder.setPayType((byte) payType);
            siJieMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            siJieMallOrder.setPayTime(new Date());
            siJieMallOrder.setUpdateTime(new Date());
            if (siJieMallOrderMapper.updateByPrimaryKeySelective(siJieMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<SiJieMallOrderItemVO> getOrderItems(Long id) {
        SiJieMallOrder siJieMallOrder = siJieMallOrderMapper.selectByPrimaryKey(id);
        if (siJieMallOrder != null) {
            List<SiJieMallOrderItem> orderItems = siJieMallOrderItemMapper.selectByOrderId(siJieMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<SiJieMallOrderItemVO> siJieMallOrderItemVOS = BeanUtil.copyList(orderItems, SiJieMallOrderItemVO.class);
                return siJieMallOrderItemVOS;
            }
        }
        return null;
    }
}
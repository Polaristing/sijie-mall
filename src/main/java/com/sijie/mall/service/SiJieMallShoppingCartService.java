package com.sijie.mall.service;

import com.sijie.mall.entity.SiJieMallShoppingCartItem;
import com.sijie.mall.controller.vo.SiJieMallShoppingCartItemVO;

import java.util.List;

public interface SiJieMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param siJieMallShoppingCartItem
     * @return
     */
    String saveSiJieMallCartItem(SiJieMallShoppingCartItem siJieMallShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param siJieMallShoppingCartItem
     * @return
     */
    String updateSiJieMallCartItem(SiJieMallShoppingCartItem siJieMallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param siJieMallShoppingCartItemId
     * @return
     */
    SiJieMallShoppingCartItem getSiJieMallCartItemById(Long siJieMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param siJieMallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long siJieMallShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param siJieMallUserId
     * @return
     */
    List<SiJieMallShoppingCartItemVO> getMyShoppingCartItems(Long siJieMallUserId);
}

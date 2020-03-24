package com.sijie.mall.dao;

import com.sijie.mall.entity.SiJieMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SiJieMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(SiJieMallShoppingCartItem record);

    int insertSelective(SiJieMallShoppingCartItem record);

    SiJieMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    SiJieMallShoppingCartItem selectByUserIdAndGoodsId(@Param("siJieMallUserId") Long siJieMallUserId, @Param("goodsId") Long goodsId);

    List<SiJieMallShoppingCartItem> selectByUserId(@Param("siJieMallUserId") Long siJieMallUserId, @Param("number") int number);

    int selectCountByUserId(Long siJieMallUserId);

    int updateByPrimaryKeySelective(SiJieMallShoppingCartItem record);

    int updateByPrimaryKey(SiJieMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}
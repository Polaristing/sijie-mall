package com.sijie.mall.service.impl;

import com.sijie.mall.common.Constants;
import com.sijie.mall.common.ServiceResultEnum;
import com.sijie.mall.dao.SiJieMallGoodsMapper;
import com.sijie.mall.dao.SiJieMallShoppingCartItemMapper;
import com.sijie.mall.entity.SiJieMallGoods;
import com.sijie.mall.entity.SiJieMallShoppingCartItem;
import com.sijie.mall.service.SiJieMallShoppingCartService;
import com.sijie.mall.util.BeanUtil;
import com.sijie.mall.controller.vo.SiJieMallShoppingCartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SiJieMallShoppingCartServiceImpl implements SiJieMallShoppingCartService {

    @Autowired
    private SiJieMallShoppingCartItemMapper siJieMallShoppingCartItemMapper;

    @Autowired
    private SiJieMallGoodsMapper siJieMallGoodsMapper;

    //todo 修改session中购物项数量

    @Override
    public String saveSiJieMallCartItem(SiJieMallShoppingCartItem siJieMallShoppingCartItem) {
        SiJieMallShoppingCartItem temp = siJieMallShoppingCartItemMapper.selectByUserIdAndGoodsId(siJieMallShoppingCartItem.getUserId(), siJieMallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(siJieMallShoppingCartItem.getGoodsCount());
            return updateSiJieMallCartItem(temp);
        }
        SiJieMallGoods siJieMallGoods = siJieMallGoodsMapper.selectByPrimaryKey(siJieMallShoppingCartItem.getGoodsId());
        //商品为空
        if (siJieMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = siJieMallShoppingCartItemMapper.selectCountByUserId(siJieMallShoppingCartItem.getUserId());
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (siJieMallShoppingCartItemMapper.insertSelective(siJieMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateSiJieMallCartItem(SiJieMallShoppingCartItem siJieMallShoppingCartItem) {
        SiJieMallShoppingCartItem siJieMallShoppingCartItemUpdate = siJieMallShoppingCartItemMapper.selectByPrimaryKey(siJieMallShoppingCartItem.getCartItemId());
        if (siJieMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (siJieMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        siJieMallShoppingCartItemUpdate.setGoodsCount(siJieMallShoppingCartItem.getGoodsCount());
        siJieMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (siJieMallShoppingCartItemMapper.updateByPrimaryKeySelective(siJieMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SiJieMallShoppingCartItem getSiJieMallCartItemById(Long siJieMallShoppingCartItemId) {
        return siJieMallShoppingCartItemMapper.selectByPrimaryKey(siJieMallShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long siJieMallShoppingCartItemId) {
        //todo userId不同不能删除
        return siJieMallShoppingCartItemMapper.deleteByPrimaryKey(siJieMallShoppingCartItemId) > 0;
    }

    @Override
    public List<SiJieMallShoppingCartItemVO> getMyShoppingCartItems(Long siJieMallUserId) {
        List<SiJieMallShoppingCartItemVO> siJieMallShoppingCartItemVOS = new ArrayList<>();
        List<SiJieMallShoppingCartItem> siJieMallShoppingCartItems = siJieMallShoppingCartItemMapper.selectByUserId(siJieMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(siJieMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> siJieMallGoodsIds = siJieMallShoppingCartItems.stream().map(SiJieMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<SiJieMallGoods> siJieMallGoods = siJieMallGoodsMapper.selectByPrimaryKeys(siJieMallGoodsIds);
            Map<Long, SiJieMallGoods> siJieMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(siJieMallGoods)) {
                siJieMallGoodsMap = siJieMallGoods.stream().collect(Collectors.toMap(SiJieMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (SiJieMallShoppingCartItem siJieMallShoppingCartItem : siJieMallShoppingCartItems) {
                SiJieMallShoppingCartItemVO siJieMallShoppingCartItemVO = new SiJieMallShoppingCartItemVO();
                BeanUtil.copyProperties(siJieMallShoppingCartItem, siJieMallShoppingCartItemVO);
                if (siJieMallGoodsMap.containsKey(siJieMallShoppingCartItem.getGoodsId())) {
                    SiJieMallGoods siJieMallGoodsTemp = siJieMallGoodsMap.get(siJieMallShoppingCartItem.getGoodsId());
                    siJieMallShoppingCartItemVO.setGoodsCoverImg(siJieMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = siJieMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    siJieMallShoppingCartItemVO.setGoodsName(goodsName);
                    siJieMallShoppingCartItemVO.setSellingPrice(siJieMallGoodsTemp.getSellingPrice());
                    siJieMallShoppingCartItemVOS.add(siJieMallShoppingCartItemVO);
                }
            }
        }
        return siJieMallShoppingCartItemVOS;
    }
}

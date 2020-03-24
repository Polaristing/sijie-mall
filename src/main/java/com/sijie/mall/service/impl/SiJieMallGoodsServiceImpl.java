package com.sijie.mall.service.impl;

import com.sijie.mall.common.ServiceResultEnum;
import com.sijie.mall.dao.SiJieMallGoodsMapper;
import com.sijie.mall.entity.SiJieMallGoods;
import com.sijie.mall.service.SiJieMallGoodsService;
import com.sijie.mall.util.BeanUtil;
import com.sijie.mall.util.PageQueryUtil;
import com.sijie.mall.util.PageResult;
import com.sijie.mall.controller.vo.SiJieMallSearchGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SiJieMallGoodsServiceImpl implements SiJieMallGoodsService {

    @Autowired
    private SiJieMallGoodsMapper goodsMapper;

    @Override
    public PageResult getSiJieMallGoodsPage(PageQueryUtil pageUtil) {
        List<SiJieMallGoods> goodsList = goodsMapper.findSiJieMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalSiJieMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveSiJieMallGoods(SiJieMallGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveSiJieMallGoods(List<SiJieMallGoods> siJieMallGoodsList) {
        if (!CollectionUtils.isEmpty(siJieMallGoodsList)) {
            goodsMapper.batchInsert(siJieMallGoodsList);
        }
    }

    @Override
    public String updateSiJieMallGoods(SiJieMallGoods goods) {
        SiJieMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public SiJieMallGoods getSiJieMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchSiJieMallGoods(PageQueryUtil pageUtil) {
        List<SiJieMallGoods> goodsList = goodsMapper.findSiJieMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalSiJieMallGoodsBySearch(pageUtil);
        List<SiJieMallSearchGoodsVO> siJieMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            siJieMallSearchGoodsVOS = BeanUtil.copyList(goodsList, SiJieMallSearchGoodsVO.class);
            for (SiJieMallSearchGoodsVO siJieMallSearchGoodsVO : siJieMallSearchGoodsVOS) {
                String goodsName = siJieMallSearchGoodsVO.getGoodsName();
                String goodsIntro = siJieMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    siJieMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    siJieMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(siJieMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}

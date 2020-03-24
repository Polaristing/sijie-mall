package com.sijie.mall.service;

import com.sijie.mall.entity.SiJieMallGoods;
import com.sijie.mall.util.PageQueryUtil;
import com.sijie.mall.util.PageResult;

import java.util.List;

public interface SiJieMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getSiJieMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveSiJieMallGoods(SiJieMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param siJieMallGoodsList
     * @return
     */
    void batchSaveSiJieMallGoods(List<SiJieMallGoods> siJieMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateSiJieMallGoods(SiJieMallGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    SiJieMallGoods getSiJieMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchSiJieMallGoods(PageQueryUtil pageUtil);
}

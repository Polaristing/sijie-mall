package com.sijie.mall.service;

import com.sijie.mall.entity.IndexConfig;
import com.sijie.mall.util.PageQueryUtil;
import com.sijie.mall.util.PageResult;
import com.sijie.mall.controller.vo.SiJieMallIndexConfigGoodsVO;

import java.util.List;

public interface SiJieMallIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SiJieMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}

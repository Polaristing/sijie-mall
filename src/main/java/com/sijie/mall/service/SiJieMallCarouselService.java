package com.sijie.mall.service;

import com.sijie.mall.entity.Carousel;
import com.sijie.mall.util.PageQueryUtil;
import com.sijie.mall.util.PageResult;
import com.sijie.mall.controller.vo.SiJieMallIndexCarouselVO;

import java.util.List;

public interface SiJieMallCarouselService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<SiJieMallIndexCarouselVO> getCarouselsForIndex(int number);
}

package com.sijie.mall.controller.mall;

import com.sijie.mall.common.Constants;
import com.sijie.mall.common.IndexConfigTypeEnum;
import com.sijie.mall.controller.vo.SiJieMallIndexCarouselVO;
import com.sijie.mall.controller.vo.SiJieMallIndexCategoryVO;
import com.sijie.mall.controller.vo.SiJieMallIndexConfigGoodsVO;
import com.sijie.mall.service.SiJieMallCarouselService;
import com.sijie.mall.service.SiJieMallCategoryService;
import com.sijie.mall.service.SiJieMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Kim
 * @联系QQ 1172895463
 * @email gting0518@163.com
 * @link https://www.xiayuan52.cn
 */
@Controller
public class IndexController {

    @Resource
    private SiJieMallCarouselService siJieMallCarouselService;

    @Resource
    private SiJieMallIndexConfigService siJieMallIndexConfigService;

    @Resource
    private SiJieMallCategoryService siJieMallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<SiJieMallIndexCategoryVO> categories = siJieMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<SiJieMallIndexCarouselVO> carousels = siJieMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<SiJieMallIndexConfigGoodsVO> hotGoodses = siJieMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<SiJieMallIndexConfigGoodsVO> newGoodses = siJieMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<SiJieMallIndexConfigGoodsVO> recommendGoodses = siJieMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}

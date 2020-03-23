package com.sijie.mall.controller.vo;

import java.io.Serializable;


/**
 * @author Kim
 * @联系QQ 1172895463
 * @email 1172895463@qq.com
 * @link https://www.xiayuan52.cn
 * @apiNote 首页轮播图VO
 */
public class NewBeeMallIndexCarouselVO implements Serializable {

    private String carouselUrl;

    private String redirectUrl;

    public String getCarouselUrl() {
        return carouselUrl;
    }

    public void setCarouselUrl(String carouselUrl) {
        this.carouselUrl = carouselUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}

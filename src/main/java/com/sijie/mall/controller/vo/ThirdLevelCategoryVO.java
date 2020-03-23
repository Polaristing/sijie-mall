package com.sijie.mall.controller.vo;

import java.io.Serializable;

/**
 * @author Kim
 * @联系QQ 1172895463
 * @email 1172895463@qq.com
 * @link https://www.xiayuan52.cn
 * @apiNote 首页分类数据VO(第三级)
 */
public class ThirdLevelCategoryVO implements Serializable {

    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Byte categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

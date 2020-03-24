package com.sijie.mall.common;

/**
 * @author Kim
 * @联系QQ 1172895463
 * @email gting0518@163.com
 * @link https://www.xiayuan52.cn
 * @apiNote 分类级别
 */
public enum SiJieMallCategoryLevelEnum {

    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "一级分类"),
    LEVEL_TWO(2, "二级分类"),
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    SiJieMallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static SiJieMallCategoryLevelEnum getSiJieMallOrderStatusEnumByLevel(int level) {
        for (SiJieMallCategoryLevelEnum siJieMallCategoryLevelEnum : SiJieMallCategoryLevelEnum.values()) {
            if (siJieMallCategoryLevelEnum.getLevel() == level) {
                return siJieMallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

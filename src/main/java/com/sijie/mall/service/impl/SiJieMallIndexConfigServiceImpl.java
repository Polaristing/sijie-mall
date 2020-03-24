package com.sijie.mall.service.impl;

import com.sijie.mall.common.ServiceResultEnum;
import com.sijie.mall.dao.IndexConfigMapper;
import com.sijie.mall.dao.SiJieMallGoodsMapper;
import com.sijie.mall.entity.IndexConfig;
import com.sijie.mall.entity.SiJieMallGoods;
import com.sijie.mall.service.SiJieMallIndexConfigService;
import com.sijie.mall.util.BeanUtil;
import com.sijie.mall.util.PageQueryUtil;
import com.sijie.mall.util.PageResult;
import com.sijie.mall.controller.vo.SiJieMallIndexConfigGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiJieMallIndexConfigServiceImpl implements SiJieMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private SiJieMallGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //todo 判断是否存在该商品
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<SiJieMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<SiJieMallIndexConfigGoodsVO> siJieMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<SiJieMallGoods> siJieMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            siJieMallIndexConfigGoodsVOS = BeanUtil.copyList(siJieMallGoods, SiJieMallIndexConfigGoodsVO.class);
            for (SiJieMallIndexConfigGoodsVO siJieMallIndexConfigGoodsVO : siJieMallIndexConfigGoodsVOS) {
                String goodsName = siJieMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = siJieMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    siJieMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    siJieMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return siJieMallIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}

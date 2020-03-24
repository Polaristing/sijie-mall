package com.sijie.mall.dao;

import com.sijie.mall.entity.SiJieMallGoods;
import com.sijie.mall.entity.StockNumDTO;
import com.sijie.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SiJieMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(SiJieMallGoods record);

    int insertSelective(SiJieMallGoods record);

    SiJieMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(SiJieMallGoods record);

    int updateByPrimaryKeyWithBLOBs(SiJieMallGoods record);

    int updateByPrimaryKey(SiJieMallGoods record);

    List<SiJieMallGoods> findSiJieMallGoodsList(PageQueryUtil pageUtil);

    int getTotalSiJieMallGoods(PageQueryUtil pageUtil);

    List<SiJieMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<SiJieMallGoods> findSiJieMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalSiJieMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("siJieMallGoodsList") List<SiJieMallGoods> siJieMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}
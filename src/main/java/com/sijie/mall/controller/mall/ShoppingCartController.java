package com.sijie.mall.controller.mall;

import com.sijie.mall.common.Constants;
import com.sijie.mall.common.ServiceResultEnum;
import com.sijie.mall.controller.vo.SiJieMallShoppingCartItemVO;
import com.sijie.mall.controller.vo.SiJieMallUserVO;
import com.sijie.mall.entity.SiJieMallShoppingCartItem;
import com.sijie.mall.service.SiJieMallShoppingCartService;
import com.sijie.mall.util.Result;
import com.sijie.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Kim
 * @联系QQ 1172895463
 * @email gting0518@163.com
 * @link https://www.xiayuan52.cn
 */
@Controller
public class ShoppingCartController {

    @Resource
    private SiJieMallShoppingCartService siJieMallShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        SiJieMallUserVO user = (SiJieMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<SiJieMallShoppingCartItemVO> myShoppingCartItems = siJieMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //购物项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(SiJieMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (SiJieMallShoppingCartItemVO siJieMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += siJieMallShoppingCartItemVO.getGoodsCount() * siJieMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveSiJieMallShoppingCartItem(@RequestBody SiJieMallShoppingCartItem siJieMallShoppingCartItem,
                                                 HttpSession httpSession) {
        SiJieMallUserVO user = (SiJieMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        siJieMallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = siJieMallShoppingCartService.saveSiJieMallCartItem(siJieMallShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateSiJieMallShoppingCartItem(@RequestBody SiJieMallShoppingCartItem siJieMallShoppingCartItem,
                                                   HttpSession httpSession) {
        SiJieMallUserVO user = (SiJieMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        siJieMallShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String updateResult = siJieMallShoppingCartService.updateSiJieMallCartItem(siJieMallShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{siJieMallShoppingCartItemId}")
    @ResponseBody
    public Result updateSiJieMallShoppingCartItem(@PathVariable("siJieMallShoppingCartItemId") Long siJieMallShoppingCartItemId,
                                                   HttpSession httpSession) {
        SiJieMallUserVO user = (SiJieMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = siJieMallShoppingCartService.deleteById(siJieMallShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        SiJieMallUserVO user = (SiJieMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<SiJieMallShoppingCartItemVO> myShoppingCartItems = siJieMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (SiJieMallShoppingCartItemVO siJieMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += siJieMallShoppingCartItemVO.getGoodsCount() * siJieMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}

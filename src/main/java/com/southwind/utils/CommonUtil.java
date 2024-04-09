package com.southwind.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.Cart;
import com.southwind.entity.Product;
import com.southwind.entity.User;
import com.southwind.exception.MMallException;
import com.southwind.result.ResponseEnum;
import com.southwind.service.CartService;
import com.southwind.service.ProductCategoryService;
import com.southwind.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CartService cartService;

    public void setCommonObject(HttpSession session,ModelAndView modelAndView) {
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            //未登录
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            //登录用户
            //查询该用户的购物车记录
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        }
        //商品分类
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
    }
}

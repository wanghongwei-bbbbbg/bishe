package com.southwind.controller;


import com.southwind.entity.User;
import com.southwind.exception.MMallException;
import com.southwind.result.ResponseEnum;
import com.southwind.service.CartService;
import com.southwind.service.OrdersService;
import com.southwind.service.UserAddressService;
import com.southwind.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-11-22
 */
@Controller
@RequestMapping("/productCategory/orders")
@Slf4j
public class OrdersController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

    /**
     * 返回所有用户的装机方案
     * @return
     */
    @GetMapping("/schemeList")
    public ModelAndView ordersList(HttpSession session){
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("schemeTable");
        modelAndView.addObject("schemeList", this.ordersService.findALL());
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;   //return modelandview；会把数据传过去，同时渲染视图
    }

}


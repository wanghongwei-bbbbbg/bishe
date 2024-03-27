package com.southwind.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.User;
import com.southwind.entity.UserAddress;
import com.southwind.exception.MMallException;
import com.southwind.form.UserLoginForm;
import com.southwind.form.UserRegisterForm;
import com.southwind.result.ResponseEnum;
import com.southwind.service.CartService;
import com.southwind.service.OrdersService;
import com.southwind.service.UserAddressService;
import com.southwind.service.UserService;
import com.southwind.utils.RegexValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-11-22
 */
@Controller
@RequestMapping("/user")//与前端页面对应，但地址栏上没有user
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

    /**
     * 用户注册
     * @param userRegisterForm
     * @param bindingResult，校验结果的封装
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterForm userRegisterForm, BindingResult bindingResult){
        //非空校验
        if(bindingResult.hasErrors()){
            log.info("【用户注册】用户信息不能为空");
            throw new MMallException(ResponseEnum.USER_INFO_NULL);
        }
        // 注册
        User register = this.userService.register(userRegisterForm);
        if(register == null){
            log.info("【用户注册】添加用户失败");
            throw new MMallException(ResponseEnum.USER_REGISTER_ERROR);
        }
        return "redirect:/login"; //如果是return login是转发，地址栏不发生改变；应该使用重定向redirect
    }

    /**
     * 用户登录
     * @return
     */
    @PostMapping("/login")
    public String login(@Valid UserLoginForm userLoginForm, BindingResult bindingResult, HttpSession session){
        //非空校验
        if(bindingResult.hasErrors()){
            log.info("【用户登录】用户信息不能为空");
            throw new MMallException(ResponseEnum.USER_INFO_NULL);
        }
        User login = this.userService.login(userLoginForm);
        session.setAttribute("user",login); //传入用户数据
        return "redirect:/productCategory/main"; //跳到请求/productCategory/main；所以还要写controller
        //return modelandview；会把数据传过去，同时渲染视图
    }

    /**
     * 返回当前用户的订单列表
     * @return
     */
    @GetMapping("/orderList")
    public ModelAndView ordersList(HttpSession session){
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orderList");
        modelAndView.addObject("orderList", this.ordersService.findAllByUserId(user.getId()));
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;   //return modelandview；会把数据传过去，同时渲染视图
    }

    /**
     * 返回当前用户的地址列表
     * @return
     */
    @GetMapping("/addressList")
    public ModelAndView addressList(HttpSession session){
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userAddressList");
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList", this.userAddressService.list(queryWrapper));
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }
}


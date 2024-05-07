package com.southwind.controller;


import com.southwind.entity.Orders;
import com.southwind.entity.Schemes;
import com.southwind.entity.User;
import com.southwind.exception.MMallException;
import com.southwind.result.ResponseEnum;
import com.southwind.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-04-19
 */
@Controller
@Slf4j
@RequestMapping("/schemes")
public class SchemesController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CompatibilityService compatibilityService;
    @Autowired
    private SchemesService schemesService;

    /**
     * 发布装机方案
     *
     * @param
     * @param session
     * @return
     */
    @GetMapping("/upload/{id}")
    public ModelAndView upload(@PathVariable("id") Integer id, HttpSession session) {
        String schemeName = "5000元以下最好的装机方案";
        if (schemeName == null ) {
            log.info("【更新购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("schemeUpload");
        Schemes schemes = this.schemesService.upload(schemeName,user,id);
        if (schemes != null) {
            modelAndView.addObject("schemes", schemes);
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
            return modelAndView;
        }
        return null;
    }
}


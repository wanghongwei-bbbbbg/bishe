package com.southwind.controller;


import com.southwind.service.CartService;
import com.southwind.service.CompatibilityService;
import com.southwind.service.ProductService;
import com.southwind.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-04-12
 */
@Controller
@RequestMapping("//compatibility")
public class CompatibilityController {

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

}


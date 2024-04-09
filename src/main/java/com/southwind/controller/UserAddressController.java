package com.southwind.controller;


import com.southwind.service.CartService;
import com.southwind.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-11-22
 */

@Controller
@RequestMapping("/userAddress")
public class UserAddressController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

}


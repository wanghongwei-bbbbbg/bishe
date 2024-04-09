package com.southwind.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.Cart;
import com.southwind.entity.Orders;
import com.southwind.entity.Product;
import com.southwind.entity.User;
import com.southwind.exception.MMallException;
import com.southwind.result.ResponseEnum;
import com.southwind.service.CartService;
import com.southwind.service.ProductService;
import com.southwind.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-11-22
 */
@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private ProductService productService;

    @GetMapping("/diy/{type}/{id}")
    public ModelAndView diy(
            @PathVariable("type") Integer type,
            @PathVariable("id") Integer productCategoryId,
            HttpSession session){
        if(type == null || productCategoryId == null){
            log.info("【商品列表】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("diy");
        modelAndView.addObject("productList", this.productService.findAllByTypeAndProductCategoryId(type, productCategoryId));
        return modelAndView;
    }
    /**
     * 添加购物车
     * @return
     */
    @GetMapping("/add/{productId}/{price}/{quantity}")
    public String add(
            HttpServletResponse response,
            @PathVariable("productId") Integer productId,
            @PathVariable("price") Float price,
            @PathVariable("quantity") Integer quantity,
            HttpSession session
    ){
        if(productId == null || price == null || quantity == null){
            log.info("【添加购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【添加购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setCost(price * quantity);
        //通过productid，找 leveloneid
        Product product = this.productService.findByProductId(productId);
        Integer categoryleveloneId = product.getCategoryleveloneId();

        boolean flagValue = true;
        UserController.isHave(session, flagValue, categoryleveloneId);


        Boolean add = this.cartService.add(cart);
        if(!add){
            log.info("【添加购物车】添加失败");
            throw new MMallException(ResponseEnum.CART_ADD_ERROR);
        }/*else {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                String msg = "history.go(-1);alert('添加成功')";    //回到上一个页面
                writer.print("<script type='text/javascript'>" + msg + "</script>");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        return "redirect:/product/list/1/"+categoryleveloneId;
    }

    /**
     * 查看购物车
     * @param session
     * @return
     */
    @GetMapping("/get")
    public ModelAndView get(HttpSession session){
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【添加购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement1");
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        return modelAndView;
    }

    /**
     * 更新购物车
     * @return
     */
    @PostMapping("/update/{id}/{quantity}/{cost}")
    @ResponseBody
    public String update(
            @PathVariable("id") Integer id,
            @PathVariable("quantity") Integer quantity,
            @PathVariable("cost") Float cost,
            HttpSession session
    ){
        if(id == null || quantity == null || cost == null){
            log.info("【更新购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        if(this.cartService.update(id, quantity, cost)) return "success";
        return "fail";
    }

    /**
     * 删除购物车
     * @param id
     * @param session
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id,HttpSession session){
        if(id == null){
            log.info("【更新购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        Boolean delete = this.cartService.delete(id);
        if(delete) return "redirect:/cart/get";
        return null;
    }

    /**
     * 确认订单
     * @param session
     * @return
     */
    @GetMapping("/confirm")
    public ModelAndView confirm(HttpSession session){
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement2");
        modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList", this.userAddressService.list(queryWrapper));
        return modelAndView;
    }

    /**
     * 确认订单
     * @param userAddress
     * @param session
     * @return
     */
    @PostMapping("/commit")
    public ModelAndView commit(
            String userAddress,
            String address,
            String remark,
            HttpSession session){
        if(userAddress == null || address == null || remark == null){
            log.info("【更新购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement3");
        Orders orders = this.cartService.commit(userAddress, address, remark, user);
        if(orders != null){
            modelAndView.addObject("orders", orders);
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
            return modelAndView;
        }
        return null;
    }
}


package com.southwind.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.*;
import com.southwind.exception.MMallException;
import com.southwind.result.ResponseEnum;
import com.southwind.service.CartService;
import com.southwind.service.CompatibilityService;
import com.southwind.service.ProductService;
import com.southwind.service.UserAddressService;
import com.southwind.utils.CommonUtil;
import com.southwind.utils.DescriptionParser;
import com.southwind.vo.CartVO;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
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
    @Autowired
    private CompatibilityService compatibilityService;

    @GetMapping("/diy/{type}/{id}")
    public ModelAndView diy(
            @PathVariable("type") Integer type,
            @PathVariable("id") Integer productCategoryId,
            HttpSession session) {
        if (type == null || productCategoryId == null) {
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
     *
     * @return
     */
    @GetMapping("/add/{productId}/{price}/{quantity}")
    public String add(
            HttpServletResponse response,
            @PathVariable("productId") Integer productId,
            @PathVariable("price") Float price,
            @PathVariable("quantity") Integer quantity,
            HttpSession session
    ) {
        Boolean flag = true;
        Boolean add = null;
        if (productId == null || price == null || quantity == null) {
            log.info("【添加购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
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

        //兼容性校验 建立在有主板的情况下
        if ((boolean) session.getAttribute("boardFlag"))
            flag = isCompatible(session, product, productId, user, categoryleveloneId);

        //进行添加操作
        if (flag)
            add = this.cartService.add(cart);


        if (!add) {
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
        //加入购物车后，设置flagvlue为true
        boolean flagValue = true;
        UserController.isHave(session, flagValue, categoryleveloneId);
        return "redirect:/product/list/1/" + categoryleveloneId;
    }

    /**
     * 查看购物车
     *
     * @param session
     * @return
     */
    @GetMapping("/get")
    public ModelAndView get(HttpSession session) {
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
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
     *
     * @return
     */
    @PostMapping("/update/{id}/{quantity}/{cost}")
    @ResponseBody
    public String update(
            @PathVariable("id") Integer id,
            @PathVariable("quantity") Integer quantity,
            @PathVariable("cost") Float cost,
            HttpSession session
    ) {
        if (id == null || quantity == null || cost == null) {
            log.info("【更新购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        if (this.cartService.update(id, quantity, cost)) return "success";
        return "fail";
    }

    /**
     * 删除购物车
     *
     * @param id
     * @param session
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, HttpSession session) {
        if (id == null) {
            log.info("【更新购物车】参数为空");
            throw new MMallException(ResponseEnum.PARAMETER_NULL);
        }
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.info("【更新购物车】当前为未登录状态");
            throw new MMallException(ResponseEnum.NOT_LOGIN);
        }
        Cart cart = this.cartService.getById(id);

        Boolean delete = this.cartService.delete(id);

        Product product = this.productService.findByProductId(cart.getProductId());
        Integer categoryleveloneId = product.getCategoryleveloneId();
        boolean flagValue = false;
        if (delete){
            CommonUtil.setSessionBydelete(session,flagValue,categoryleveloneId);
            return "redirect:/cart/get";
        }
        return null;
    }

    /**
     * 确认订单
     *
     * @param session
     * @return
     */
    @GetMapping("/confirm")
    public ModelAndView confirm(HttpSession session) {
        //判断是否为登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
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
     *
     * @param userAddress
     * @param session
     * @return
     */
    @PostMapping("/commit")
    public ModelAndView commit(
            String userAddress,
            String address,
            String remark,
            HttpSession session) {
        if (userAddress == null || address == null || remark == null) {
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
        modelAndView.setViewName("settlement3");
        Orders orders = this.cartService.commit(userAddress, address, remark, user);
        if (orders != null) {
            modelAndView.addObject("orders", orders);
            modelAndView.addObject("cartList", this.cartService.findVOListByUserId(user.getId()));
            return modelAndView;
        }
        return null;
    }

    public Boolean isCompatible(HttpSession session, Product product, Integer productId, User user, Integer categoryleveloneId) {
        Compatibility compatibility = this.compatibilityService.findByProductId(productId);
        String descriptionByBoard = "";
        String descriptionByAdd;
        Integer boardId = null;

        List<CartVO> cartVOList = this.cartService.findVOListByUserId(user.getId());
        for (CartVO cartVO : cartVOList) {
            Integer categoryleveloneId1 = cartVO.getCategoryleveloneId();
            if (categoryleveloneId1 == 628) { //找到购物车里的zhuban
                Product board = this.productService.findByProductId(cartVO.getProductId());
                descriptionByBoard = board.getDescription();
                boardId = board.getId();
                break;
            }
        }


        if (compatibility == null ||
                ((compatibility.getProductId1() == boardId && compatibility.getProductId2() == productId) &&
                (compatibility.getProductId1() == boardId && compatibility.getProductId2() == productId))) {
            //表中为空或者两个id没有吻合
            //判别商品类型
            //有主板
            Map<String, String> details = DescriptionParser.parseDescription(descriptionByBoard);
            String chipset = details.get("主芯片组");
            String memory = details.get("内存类型");
            descriptionByAdd = product.getDescription();
            if (categoryleveloneId == 548) { //add的是CPU

                Integer categoryleveltwoId = product.getCategoryleveltwoId();
                String cpuStyle = DescriptionParser.extractBrandFromChipset(chipset); //AMD 或者 Intel

                if (categoryleveltwoId == 778 || cpuStyle == "Intel") {
                    //Intel 品牌,无冲突
                    return true;
                } else {
                    //写入冲突表
                    Compatibility one = new Compatibility();
                    one.setProductId1(productId);
                    one.setProductId2(boardId);
                    one.setCompatible(0);
                    one.setReason("cpu和主板芯片不兼容");
                    Compatibility two = new Compatibility();
                    two.setProductId2(productId);
                    two.setProductId1(boardId);
                    two.setCompatible(0);
                    two.setReason("cpu和主板芯片不兼容");
                    compatibilityService.save(one);
                    compatibilityService.save(two);
                    return false;
                }
            }
            if (categoryleveloneId == 681){ //add的是ssd
                String memoryStyle = DescriptionParser.parseSSDDescription(descriptionByAdd); //DDR4 或者 DDR5
                if (memory.indexOf(memoryStyle) != -1){
                    //无冲突
                    return true;
                } else {
                    //写入冲突表
                    Compatibility one = new Compatibility();
                    one.setProductId1(productId);
                    one.setProductId2(boardId);
                    one.setCompatible(0);
                    one.setReason("主板是"+memory+"，所加购的ssd是"+memoryStyle);
                    Compatibility two = new Compatibility();
                    two.setProductId2(productId);
                    two.setProductId1(boardId);
                    two.setCompatible(0);
                    two.setReason("主板是"+memory+"，所加购的ssd是"+memoryStyle);
                    compatibilityService.save(one);
                    compatibilityService.save(two);
                    return false;
                }
            }
        } else {//表中不为空，就是有冲突
            log.info("【添加购物车】添加失败");
            throw new MMallException(ResponseEnum.CART_ADD_ERROR);
            //再在前端显示warning

        }
        return false;
    }
}


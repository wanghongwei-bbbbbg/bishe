package com.southwind.service;

import com.southwind.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.entity.Orders;
import com.southwind.entity.User;
import com.southwind.vo.CartVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
public interface CartService extends IService<Cart> {
    public Boolean add(Cart cart);
    //返回vo的集合，通过userid
    public List<CartVO> findVOListByUserId(Integer userId);
    public Boolean update(Integer id,Integer quantity,Float cost);
    public Boolean delete(Integer id);
    public Orders commit(String userAddress, String address, String remark, User user);
}

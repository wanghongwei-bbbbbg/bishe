package com.southwind.service;

import com.southwind.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.vo.OrdersVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
public interface OrdersService extends IService<Orders> {
    public List<OrdersVO> findAllByUserId(Integer id);

    public List<OrdersVO> findALL();
}

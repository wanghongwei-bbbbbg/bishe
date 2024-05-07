package com.southwind.mapper;

import com.southwind.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    public Float getCostById(Integer id);
}

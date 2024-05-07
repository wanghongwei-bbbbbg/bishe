package com.southwind.mapper;

import com.southwind.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
public interface ProductMapper extends BaseMapper<Product> {
    public Integer updateStockById(Integer id,Integer stock);
    public Integer getStockById(Integer id);

}

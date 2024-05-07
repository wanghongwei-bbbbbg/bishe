package com.southwind.service;

import com.southwind.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
public interface ProductService extends IService<Product> {
    public List<Product> findAllByTypeAndProductCategoryId(Integer type,Integer id);
    public Product findByProductId(Integer productId);

}

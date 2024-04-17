package com.southwind.service;

import com.southwind.entity.Compatibility;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-04-12
 */
public interface CompatibilityService extends IService<Compatibility> {

    public Compatibility findByProductId(Integer productId);
}

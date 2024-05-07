package com.southwind.service;

import com.southwind.entity.Orders;
import com.southwind.entity.Schemes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.southwind.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-04-19
 */
public interface SchemesService extends IService<Schemes> {

    public Schemes upload(String schemeName, User user, Integer id);
}

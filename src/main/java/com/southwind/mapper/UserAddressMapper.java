package com.southwind.mapper;

import com.southwind.entity.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    public int setDefault();
}

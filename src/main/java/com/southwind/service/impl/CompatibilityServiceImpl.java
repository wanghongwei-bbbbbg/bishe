package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.Cart;
import com.southwind.entity.Compatibility;
import com.southwind.mapper.*;
import com.southwind.service.CompatibilityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-04-12
 */
@Service
public class CompatibilityServiceImpl extends ServiceImpl<CompatibilityMapper, Compatibility> implements CompatibilityService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private CompatibilityMapper compatibilityMapper;


    @Override
    public Compatibility findByProductId(Integer productId) {
        QueryWrapper<Compatibility> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_Id1", productId);
        Compatibility compatibility = this.compatibilityMapper.selectOne(queryWrapper);

        if (compatibility == null){
            // 为null则继续查找
            queryWrapper.eq("product_Id2", productId);
            Compatibility compatibility1 = this.compatibilityMapper.selectOne(queryWrapper);
            if (compatibility1 == null){
                //都为null，意味着表格里没有
                return null;
            }else {
                return compatibility1;
            }
        }else {
            return compatibility;
        }
    }


}

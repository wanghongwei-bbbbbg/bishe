package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.*;
import com.southwind.exception.MMallException;
import com.southwind.mapper.*;
import com.southwind.result.ResponseEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.service.SchemesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-04-19
 */
@Service
@Slf4j
public class SchemesServiceImpl extends ServiceImpl<SchemesMapper, Schemes> implements SchemesService {

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
    @Autowired
    private SchemesMapper schemesMapper;
    @Autowired
    private SchemeDetailMapper schemeDetailMapper;

    @Override
    @Transactional
    public Schemes upload(String schemeName, User user, Integer id) {
        //创建订单主表
        Schemes schemes = new Schemes();
        schemes.setUserId(user.getId());
        schemes.setLoginName(user.getLoginName());
        schemes.setSchemesName(schemeName);
        schemes.setCost(this.ordersMapper.getCostById(id));
        String seriaNumber = null;
        //创建订单号，不重复
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<32;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber =  result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        schemes.setSerialnumber(seriaNumber);

        int insert = this.schemesMapper.insert(schemes);
        if(insert != 1){
            log.info("【上传装机方案】创建装机方案主表失败");
            throw new MMallException(ResponseEnum.ORDERS_CREATE_ERROR);
        }
        //创建装机方案从表
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        List<OrderDetail> orderDetails = this.orderDetailMapper.selectList(queryWrapper);
        for (OrderDetail orderDetail : orderDetails) {
            SchemeDetail schemeDetail = new SchemeDetail();
            BeanUtils.copyProperties(orderDetail, schemeDetail);
            schemeDetail.setSchemeId(schemes.getId());
            int insert1 = this.schemeDetailMapper.insert(schemeDetail);
            if(insert1 == 0){
                log.info("【上传装机方案】创建装机方案详情失败");
                throw new MMallException(ResponseEnum.ORDER_DETAIL_CREATE_ERROR);
            }
        }
        return schemes;
    }
}

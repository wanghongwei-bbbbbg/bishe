package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.OrderDetail;
import com.southwind.entity.Orders;
import com.southwind.entity.Product;
import com.southwind.mapper.OrderDetailMapper;
import com.southwind.mapper.OrdersMapper;
import com.southwind.mapper.ProductMapper;
import com.southwind.mapper.SchemeMapper;
import com.southwind.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.southwind.vo.OrderDetailVO;
import com.southwind.vo.OrdersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-3-18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SchemeMapper schemeMapper;

    @Override
    public List<OrdersVO> findAllByUserId(Integer id) {
        //查询user的order
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        List<Orders> ordersList = this.ordersMapper.selectList(queryWrapper);
        //转换为vo类型
        List<OrdersVO> ordersVOList = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersVO ordersVO = new OrdersVO();
            //将orders的属性传到vo里
            BeanUtils.copyProperties(orders, ordersVO);
            //查询里面的detail从表里面的数据
            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", orders.getId());
            List<OrderDetail> orderDetailList = this.orderDetailMapper.selectList(queryWrapper1);
            //detail转换为vo类型
            List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetailList) {
                OrderDetailVO orderDetailVO = new OrderDetailVO();
                BeanUtils.copyProperties(orderDetail, orderDetailVO);
                Product product = this.productMapper.selectById(orderDetail.getProductId());
                BeanUtils.copyProperties(product, orderDetailVO);
                orderDetailVOList.add(orderDetailVO);
            }
            //赋值vo属性
            ordersVO.setOrderDetailList(orderDetailVOList);
            ordersVOList.add(ordersVO);
        }

        return ordersVOList;
    }

    @Override
    public List<OrdersVO> findALL() {
        //查询所有用户发布的装机方案
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        List<Orders> ordersList = this.ordersMapper.selectList(queryWrapper);
        //转换为vo类型
        List<OrdersVO> ordersVOList = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrdersVO ordersVO = new OrdersVO();
            //将orders的属性传到vo里
            BeanUtils.copyProperties(orders, ordersVO);
            //查询里面的detail从表里面的数据
            QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", orders.getId());
            List<OrderDetail> orderDetailList = this.orderDetailMapper.selectList(queryWrapper1);
            //detail转换为vo类型
            List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetailList) {
                OrderDetailVO orderDetailVO = new OrderDetailVO();
                BeanUtils.copyProperties(orderDetail, orderDetailVO);
                Product product = this.productMapper.selectById(orderDetail.getProductId());
                BeanUtils.copyProperties(product, orderDetailVO);
                orderDetailVOList.add(orderDetailVO);
            }
            //赋值vo属性
            ordersVO.setOrderDetailList(orderDetailVOList);
            ordersVOList.add(ordersVO);
        }

        return ordersVOList;
    }
}

package com.southwind.vo;

import lombok.Data;

/**
 * vo是适配前端页面的实体数据类型
 */
@Data
public class CartVO {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Float cost;
    private String name;
    private String fileName;
    private Float price;
    private Integer stock;
}

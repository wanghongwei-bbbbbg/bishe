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
    /**
     * 分类1
     */
    private Integer categoryleveloneId;

    /**
     * 分类2
     */
    private Integer categoryleveltwoId;

    /**
     * 分类3
     */
    private Integer categorylevelthreeId;
    private Float cost;
    private String name; //商品名称
    private String fileName;
    private Float price;
    private Integer stock;
}

package com.southwind.vo;

import com.southwind.entity.Product;
import com.southwind.entity.ProductCategory;
import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVO {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<ProductCategoryVO> children;
    private List<Product> productList;

    public ProductCategoryVO(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
        this.parentId = productCategory.getParentId();
        //因为需要转换成原始数据类型，所以就没有list 的两个属性
    }
}

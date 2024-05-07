package com.southwind.vo;

import lombok.Data;

import java.util.List;

@Data
public class SchemesVO {
    private Integer id;
    private String loginName;
    private String schemeName;
    private Float cost;
    private String serialnumber;
    private List<SchemeDetailVO> schemeDetailList;
}

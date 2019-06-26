package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProductVO {

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;


}

package com.example.waimai.dto;


import com.example.waimai.entity.Dish;
import com.example.waimai.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户展示层和数据层比较复杂数据的传输
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();
    private Integer copies;


}

package com.example.waimai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.waimai.dto.DishDto;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.DishFlavor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    DishDto getWithFlavor(Dish dish);
    void updateWithFlavor(DishDto dishDto);
    void deleteWithFlavor(Long  id);
}

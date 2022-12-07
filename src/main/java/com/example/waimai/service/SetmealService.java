package com.example.waimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.waimai.dto.SetmealDto;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Setmeal;


public interface SetmealService extends IService<Setmeal> {
    void saveWithDishes(SetmealDto setmealDto);
    void removeByIdWithDishes(Long id);
}

package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.dto.SetmealDto;
import com.example.waimai.entity.Setmeal;
import com.example.waimai.entity.SetmealDish;
import com.example.waimai.mapper.SetmealDishMapper;
import com.example.waimai.mapper.SetmealMapper;
import com.example.waimai.service.SetmealDishService;
import com.example.waimai.service.SetmealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

//    @Transactional
//    @Override
//    public void saveWithFishes(SetmealDto setmealDto) {
//        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
//
//    }
}

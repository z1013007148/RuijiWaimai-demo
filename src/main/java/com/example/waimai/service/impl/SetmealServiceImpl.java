package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.dto.SetmealDto;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Setmeal;
import com.example.waimai.entity.SetmealDish;
import com.example.waimai.mapper.DishMapper;
import com.example.waimai.mapper.SetmealMapper;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.DishService;
import com.example.waimai.service.SetmealDishService;
import com.example.waimai.service.SetmealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    final SetmealDishService setmealDishService;
    public SetmealServiceImpl(SetmealDishServiceImpl setmealDishService){
        this.setmealDishService = setmealDishService;
    }

    @Transactional
    @Override
    public void saveWithDishes(SetmealDto setmealDto) {
        this.save((Setmeal)setmealDto); // 必须先保存，再给setmealdish赋setmeal的id，因为插入setmeal后才会产生id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for(SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        }


    }

    @Transactional
    @Override
    public void removeByIdWithDishes(Long id){
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDishService.remove(lambdaQueryWrapper);
        this.removeById(id);
    }

}

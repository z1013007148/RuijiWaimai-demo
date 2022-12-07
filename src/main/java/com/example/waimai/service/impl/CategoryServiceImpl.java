package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.common.CustomException;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Setmeal;
import com.example.waimai.mapper.CategoryMapper;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.DishService;
import com.example.waimai.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    final DishService dishService;
    final SetmealService setmealService;

    public CategoryServiceImpl(DishService dishService, SetmealService setmealService){
        this.dishService = dishService;
        this.setmealService = setmealService;
    }


    @Override
    public boolean remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        //检查是否关联dish
        int count = dishService.count(dishQueryWrapper);
        if(count>0){
            throw new CustomException("已关联菜品，无法删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        //检查是否关联setmeal
        count = setmealService.count(setmealLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("已关联套餐，无法删除");
        }
        return super.removeById(id);//正常删除
    }
}

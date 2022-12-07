package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.dto.DishDto;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.DishFlavor;
import com.example.waimai.mapper.CategoryMapper;
import com.example.waimai.mapper.DishMapper;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.DishFlavorService;
import com.example.waimai.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    final DishFlavorService dishFlavorService;
    public DishServiceImpl(DishFlavorService dishFlavorService){
        this.dishFlavorService = dishFlavorService;
    }


    /**
     * 将dishDto里的List<DishFlavor>解出来，并将dish存表，flavor设置dish_id后存表，由于涉及两张表，所以用事务保持一致性
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto){
        this.save(dishDto); //dishDto是dish的子类，直接存
        //flavors要赋id再存
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((x)->{
            x.setDishId(dishDto.getId());
            return x;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public DishDto getWithFlavor(Dish dish) {
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor f:flavors){
            DishFlavor oldDishFlavor = dishFlavorService.getById(f.getId());
            if(!oldDishFlavor.equals(f)){
                dishFlavorService.updateById(f);
            }
        }
        this.updateById((Dish)dishDto);

    }

    @Transactional
    @Override
    public void deleteWithFlavor(Long id){
        //删除相关dishFlavor
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        //删除dish
        this.removeById(id);

    }

}

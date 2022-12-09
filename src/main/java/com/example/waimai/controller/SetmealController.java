package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.waimai.common.CustomException;
import com.example.waimai.common.Result;
import com.example.waimai.dto.DishDto;
import com.example.waimai.dto.SetmealDto;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Setmeal;
import com.example.waimai.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    final DishService dishService;
    final SetmealService setmealService;
    final SetmealDishService setmealDishService;
    final CategoryService categoryService;
    final RedisTemplate redisTemplate;

    public SetmealController(RedisTemplate redisTemplate, DishService dishService, SetmealService setmealService, SetmealDishService setmealDishService, CategoryService categoryService) {
        this.dishService = dishService;
        this.setmealService = setmealService;
        this.setmealDishService = setmealDishService;
        this.categoryService = categoryService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 添加套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        Category category = categoryService.getById(setmealDto.getCategoryId());
        setmealDto.setCategoryName(category.getName());
        //更新sql
        setmealService.saveWithDishes(setmealDto);

        Set keys = redisTemplate.keys("setmeal_"+setmealDto.getCategoryId()+"*");
        redisTemplate.delete(keys);
        return Result.success(null, "新增菜品成功");
    }

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public Result<Page<Setmeal>> getByPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(currentPage, pageSize);
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Setmeal::getSort);
        setmealService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo, "分页查询成功");
    }

//    /**
//     * 查询符合条件的列表
//     * @param type
//     * @return
//     */
//    @GetMapping("/list/{type}")
//    public Result<List<Setmeal>> getList(@PathVariable String type){
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(type!=null,Setmeal::getType, type);
//        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
//        List<Setmeal> list = setmealService.list(queryWrapper);
//
//        return Result.success(list, "查询列表成功");
//    }

    @GetMapping("/list")
    public Result<List<Setmeal>> getList2(Setmeal setmeal) {
        String type = setmeal.getType();
        Long categoryId = setmeal.getCategoryId();
        String redisKey = "setmeal_" + categoryId + "_" + setmeal.getStatus();
        List<Setmeal> setmeals = null;
        setmeals = (List<Setmeal>) redisTemplate.opsForValue().get(redisKey);
        if (setmeals == null) { // redis中没有缓存
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getStatus, 1);
            queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
            queryWrapper.eq(type != null, Setmeal::getType, type);
            queryWrapper.orderByDesc(Setmeal::getUpdateTime);
            setmeals = setmealService.list(queryWrapper);
            redisTemplate.opsForValue().set(redisKey, setmeals);
        }


        return Result.success(setmeals, "查询列表成功");
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Setmeal> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        return Result.success(setmeal, "查询成功");
    }

    /**
     * 通过列表删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = null;

        for (Long id : ids) {
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Setmeal::getStatus, 1);
            int count = setmealService.count(lambdaQueryWrapper);//统计符合条件的数量
            if (count > 0) {
                throw new CustomException("套餐正在售卖中，不能删除");
            } else {
                Setmeal byId = setmealService.getById(id);
                Long categoryId = byId.getCategoryId();
                setmealService.removeByIdWithDishes(id);
                Set keys = redisTemplate.keys("setmeal_"+categoryId+"*");
                redisTemplate.delete(keys);
            }
        }


        return Result.success(null, "删除成功");

    }

    /**
     * 更新
     *
     * @param setmeal
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Setmeal setmeal) {
        boolean b = setmealService.updateById(setmeal);
        Set keys = redisTemplate.keys("setmeal_"+setmeal.getCategoryId()+"*");
        redisTemplate.delete(keys);
        if (b) {
            return Result.success(null, "更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

}

package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.waimai.common.Result;
import com.example.waimai.dto.DishDto;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.DishFlavor;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.DishFlavorService;
import com.example.waimai.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    final DishService dishService;
    final DishFlavorService dishFlavorService;
    final CategoryService categoryService;
    final RedisTemplate redisTemplate;

    public DishController(DishService dishService, DishFlavorService dishFlavorService, CategoryService categoryService, RedisTemplate redisTemplate) {
        this.dishService = dishService;
        this.dishFlavorService = dishFlavorService;
        this.categoryService = categoryService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        //更新sql
        Category category = categoryService.getById(dishDto.getCategoryId());
        dishDto.setCategoryName(category.getName());
        dishService.saveWithFlavor(dishDto);
        Set keys = redisTemplate.keys("dish_"+dishDto.getCategoryId()+"*");
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
    @GetMapping("/{currentPage}/{pageSize}/{name}")
    public Result<Page<Dish>> getByPage(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String name) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(currentPage, pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        if (null != name && !"undefined".equals(name)) {
            queryWrapper.like(Dish::getName, name);
        }

        dishService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo, "分页查询成功");
    }

    /**
     * 查询符合条件的列表
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> getList(Dish dish) {
        Long categoryId = dish.getCategoryId();
        List<DishDto> dtoList = null;
        List<Dish> list = null;
        String redisKey = "dish_" + categoryId + "_" + dish.getStatus();
        dtoList = (List<DishDto>) redisTemplate.opsForValue().get(redisKey);
        if (dtoList == null) { // Redis里没有，则从mysql里查
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getStatus, 1);//起售状态
            queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
            queryWrapper.orderByDesc(Dish::getSort);
            list = dishService.list(queryWrapper);
//        List<Dish> list = dishService.list(queryWrapper);
            dtoList = list.stream().map((d) -> {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(d, dishDto);
                List<DishFlavor> dishFlavors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishDto.getId()));
                dishDto.setFlavors(dishFlavors);
                return dishDto;
            }).collect(Collectors.toList());
            redisTemplate.opsForValue().set(redisKey, dtoList, 60, TimeUnit.MINUTES); // 放入Redis缓存
        }
        return Result.success(dtoList, "查询列表成功");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        DishDto dishDto = dishService.getWithFlavor(dish);
        return Result.success(dishDto, "查询成功");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        Dish byId = dishService.getById(id);
        Long categoryId = byId.getCategoryId();
        dishService.deleteWithFlavor(id);
        Set keys = redisTemplate.keys("dish_"+categoryId+"*");
        redisTemplate.delete(keys); // 所有的dish缓存都要清除
        return Result.success(null, "删除成功");

    }

    /**
     * 更新
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        Set keys = redisTemplate.keys("dish_"+dishDto.getCategoryId()+"*");
        redisTemplate.delete(keys);
        return Result.success(null, "更新成功");

    }

}

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
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    final DishService dishService;
    final DishFlavorService dishFlavorService;
    final CategoryService categoryService;
    public DishController(DishService dishService, DishFlavorService dishFlavorService, CategoryService categoryService) {
        this.dishService = dishService;
        this.dishFlavorService = dishFlavorService;
        this.categoryService = categoryService;
    }

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto){
        //更新sql
         Category category = categoryService.getById(dishDto.getCategoryId());
         dishDto.setCategoryName(category.getName());
         dishService.saveWithFlavor(dishDto);


        return Result.success(null,"新增菜品成功");

    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}/{name}")
    public Result<Page<Dish>> getByPage(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String name){
        //分页构造器
        Page<Dish> pageInfo = new Page<>(currentPage, pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        if(null!=name && !"undefined".equals(name)){
            queryWrapper.like(Dish::getName, name);
        }

        dishService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo, "分页查询成功");
    }

    /**
     * 查询符合条件的列表
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> getList(Dish dish){
        Long categoryId = dish.getCategoryId();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getStatus,1);//起售状态
        queryWrapper.eq(categoryId!=null,Dish::getCategoryId, categoryId);
        queryWrapper.orderByDesc(Dish::getSort);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtos = list.stream().map((d) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(d, dishDto);
            List<DishFlavor> dishFlavors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishDto.getId()));
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(dishDtos, "查询列表成功");
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        DishDto dishDto = dishService.getWithFlavor(dish);
        return Result.success(dishDto, "查询成功");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id){
        dishService.deleteWithFlavor(id);

        return Result.success(null, "删除成功");

    }

    /**
     * 更新
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto ){
        dishService.updateWithFlavor(dishDto);

        return Result.success(null, "更新成功");

    }

}

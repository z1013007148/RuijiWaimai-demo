package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.waimai.common.Result;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.DishFlavor;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.DishFlavorService;
import com.example.waimai.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dishFlavor")
public class DishFlavorController {

    final DishFlavorService dishFlavorService;

    public DishFlavorController(DishFlavorService dishFlavorService) {
        this.dishFlavorService = dishFlavorService;
    }

    /**
     * 添加分类
     * @param dishFlavor
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishFlavor dishFlavor){
        //更新sql
        if(dishFlavorService.save(dishFlavor)){
            return Result.success(null,"新增分类成功");
        }else{
            return Result.error("新增分类失败");
        }
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public Result<Page> getByPage(@PathVariable int currentPage, @PathVariable int pageSize){
        //分页构造器
        Page pageInfo = new Page(currentPage, pageSize);
        //条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        //排序
//        queryWrapper.orderByDesc(DishFlavor::getSort);

        dishFlavorService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo, "分页查询成功");
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishFlavor> getById(@PathVariable Long id){
        DishFlavor dishFlavor = dishFlavorService.getById(id);
        return Result.success(dishFlavor, "查询成功");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable Long id){
        boolean b = dishFlavorService.removeById(id);
        if(b){
            return Result.success(null, "删除成功");
        }else{
            return Result.error("删除失败");
        }
    }

    /**
     * 更新
     * @param dishFlavor
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishFlavor dishFlavor) {
        boolean b = dishFlavorService.updateById(dishFlavor);
        if(b){
            return Result.success(null, "更新成功");
        }else{
            return Result.error("更新失败");
        }
    }
}

package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.waimai.common.Result;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category){
        //更新sql
        if(categoryService.save(category)){
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
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //排序
        queryWrapper.orderByDesc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo, "分页查询成功");
    }

    /**
     * 查询符合条件的列表
     * @param type
     * @return
     */
    @GetMapping("/list/{type}")
    public Result<List<Category>> getList(@PathVariable String type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(type!=null, Category::getType, type);
        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);

        return Result.success(list, "查询列表成功");
    }
    /**
     * 查询列表
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> getList(){
        List<Category> list = categoryService.list();
        return Result.success(list, "查询列表成功");
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id){
        Category emp = categoryService.getById(id);
        return Result.success(emp, "查询成功");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable Long id){
        boolean b = categoryService.remove(id);
        if(b){
            return Result.success(null, "删除成功");
        }else{
            return Result.error("删除失败");
        }
    }

    /**
     * 更新
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        boolean b = categoryService.updateById(category);
        if(b){
            return Result.success(null, "更新成功");
        }else{
            return Result.error("更新失败");
        }
    }


}

package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.waimai.common.BaseContext;
import com.example.waimai.common.Result;
import com.example.waimai.entity.AddressBook;
import com.example.waimai.entity.ShoppingCart;
import com.example.waimai.service.AddressBookService;
import com.example.waimai.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    final ShoppingCartService shoppingCartService;
    public ShoppingCartController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/add")
    public Result<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();

        if(dishId!=null){ // 菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{ // 套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
        if(one!=null){ // 如果已在购物车，+1
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
        }else{ //不在购物车，先创建
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }
        return Result.success(shoppingCart, "新增地址成功");
    }

    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        if(shoppingCart.getDishId()!=null){ // 删除菜品
            Long dishId = shoppingCart.getDishId();
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{ // 删除套餐
            Long setmealId = shoppingCart.getSetmealId();
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
        one.setNumber(one.getNumber()-1);
        if(one.getNumber()>0){
            shoppingCartService.updateById(one);
        }else{
            shoppingCartService.removeById(one);
        }

        return Result.success(null, "删除成功");
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getUpdateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);
        return Result.success(shoppingCarts, "获取地址列表成功");
    }



    @DeleteMapping("/clean")
    public Result<ShoppingCart> delete(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(lambdaQueryWrapper);
        return Result.success(null, "删除地址成功");
    }


}

package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.waimai.common.BaseContext;
import com.example.waimai.common.Result;
import com.example.waimai.entity.Category;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Orders;
import com.example.waimai.service.CategoryService;
import com.example.waimai.service.OrdersService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    final OrdersService ordersService;
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * 添加orders
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        Long userId = BaseContext.getCurrentId();
        orders.setUserId(userId);
        ordersService.submit(orders);
        return Result.success(null, "提交订单成功");
    }

    @GetMapping("/userPage")
    public Result<Page<Orders>> list(@RequestParam int page, @RequestParam int pageSize){
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(Orders::getUserId, userId);
//        queryWrapper.orderByDesc()

        ordersService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo,"订单分页查询成功");
    }



}

package com.example.waimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.waimai.dto.DishDto;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.Orders;


public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}

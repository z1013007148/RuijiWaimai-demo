package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.entity.Dish;
import com.example.waimai.entity.DishFlavor;
import com.example.waimai.mapper.DishFlavorMapper;
import com.example.waimai.mapper.DishMapper;
import com.example.waimai.service.DishFlavorService;
import com.example.waimai.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

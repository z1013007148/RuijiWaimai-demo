package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.common.BaseContext;
import com.example.waimai.common.CustomException;
import com.example.waimai.dto.DishDto;
import com.example.waimai.entity.*;
import com.example.waimai.mapper.DishMapper;
import com.example.waimai.mapper.OrdersMapper;
import com.example.waimai.service.*;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    final AddressBookService addressBookService;
    final ShoppingCartService shoppingCartService;
    final UserService userService;
    final OrderDetailService orderDetailService;
    public OrdersServiceImpl(AddressBookService addressBookService, ShoppingCartService shoppingCartService, UserService userService, OrderDetailService orderDetailService){
        this.addressBookService = addressBookService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.orderDetailService = orderDetailService;
    }

    @Transactional
    @Override
    public void submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);


        if(shoppingCarts==null || shoppingCarts.size()==0){ // 购物车为空
            throw new CustomException("购物车为空，不能下单");
        }

        User user = userService.getById(userId);

        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        if(addressBook==null){
            throw new CustomException("无地址，不能下单");
        }

        long orderId = IdWorker.getId();

        // 购物车保存至订单明细，并统计总金额
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> OrderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setAmount(item.getAmount());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setOrderId(orderId);
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(OrderDetails);

        // 保存订单
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());
        orders.setCheckoutTime(LocalDateTime.now());
        this.save(orders);

        //清空购物车
        shoppingCartService.remove(lambdaQueryWrapper);


    }
}

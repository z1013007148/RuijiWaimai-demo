package com.example.waimai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.waimai.entity.AddressBook;
import com.example.waimai.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}

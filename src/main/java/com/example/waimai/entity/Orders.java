package com.example.waimai.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    // 1待付款 2待配送 3已派送 4已完成 5已取消
    private Integer status;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long addressBookId;
    private BigDecimal amount;
    private String number;
    //支付方式 1微信 2支付宝
    private Integer payMethod;
    private String remark;
    private String phone;
    private String address;
    private String userName;
    private String consignee;

//    @TableField(fill = FieldFill.INSERT)
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime checkoutTime;




}

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
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long setmealId;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long dishId;
    private BigDecimal amount;
    private Integer number;
    private String dishFlavor;
    private String image;
    private String name;


}

package com.example.waimai.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 菜品和套餐的对应关系
 */
public class SetmealDish implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private Long setmealId;
    private Long dishId;
    private Integer copies;
    private String code;
    private String image;
    private String name;
    private String type;
    private Integer sort;
    private String status;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long categoryId;
    private Float price;

    @TableField(fill = FieldFill.INSERT)
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updateTime;
    @JsonSerialize(using= ToStringSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @JsonSerialize(using= ToStringSerializer.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    private String description;
}

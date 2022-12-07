package com.example.waimai.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    private String account;
    private String password;
    private String sex;
    private String phone;
    private String idNumber;
    private String avatar;
    private String description;
    private int status;

}

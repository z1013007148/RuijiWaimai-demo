package com.example.waimai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.waimai.entity.AddressBook;
import com.example.waimai.entity.User;
import com.example.waimai.mapper.AddressBookMapper;
import com.example.waimai.mapper.UserMapper;
import com.example.waimai.service.AddressBookService;
import com.example.waimai.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}

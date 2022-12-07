package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.waimai.common.BaseContext;
import com.example.waimai.common.Result;
import com.example.waimai.dto.UserDto;
import com.example.waimai.entity.AddressBook;
import com.example.waimai.entity.AuthUtils;
import com.example.waimai.entity.User;
import com.example.waimai.service.AddressBookService;
import com.example.waimai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.jni.Address;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    final AddressBookService addressBookService;
    public AddressBookController(AddressBookService addressBookService){
        this.addressBookService = addressBookService;
    }

    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return Result.success(null, "新增地址成功");
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBooks = addressBookService.list(lambdaQueryWrapper);
        return Result.success(addressBooks, "获取地址列表成功");
    }

    @PutMapping("/default")
    public  Result<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();  // update的wrapper
        lambdaUpdateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaUpdateWrapper.set(AddressBook::getIsDefault, "0");
        addressBookService.update(lambdaUpdateWrapper); // 先全部设为非默认

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(null,"设置默认地址成功");
    }

    @GetMapping("/default")
    public  Result<AddressBook> getDefault(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, userId);
        lambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook one = addressBookService.getOne(lambdaQueryWrapper);
        return Result.success(one,"查看默认地址成功");
    }

    @DeleteMapping
    public Result<AddressBook> delete(@RequestBody AddressBook addressBook){
        addressBookService.removeById(addressBook.getId());
        return Result.success(null, "删除地址成功");
    }


}

package com.example.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.waimai.common.Result;
import com.example.waimai.entity.Employee;
import com.example.waimai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 员工登录
     * @param httpSession //session 是存储在服务器的
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpSession httpSession, @RequestBody Employee employee) {
//        1.sql先查询有没有这个账户
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp==null){
            return Result.error("不存在账户");
        }
//        2.密码在数据库中以md5加密形式存在，所以获取的密码先md5转换下
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        3.密码比对
        if(!emp.getPassword().equals(password)){
            return Result.error("密码错误");
        }
//        4.查看员工状态是否禁用
        if(emp.getStatus() == 0){
            return Result.error("账户禁用");
        }
//        5.登录成功,将员工id存入Session
        httpSession.setAttribute("employee", emp.getId()); // 为了LoginCheckFilter里能记住登录
        return Result.success(emp, "登陆成功");
    }

    /**
     * 员工退出
     * @param httpSession
     * @param
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpSession httpSession){
        httpSession.removeAttribute("employee");
        return Result.success(null, "退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @param httpSession
     * @return
     */
    @PostMapping
    public Result<String> save(HttpSession httpSession, @RequestBody Employee employee){
        //初始密码默认123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //记录当前操作的用户
        Long empId = (Long) httpSession.getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        //更新sql
        if(employeeService.save(employee)){
            return Result.success(null,"新增员工成功");
        }else{
            return Result.error("新增员工失败");
        }
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/{currentPage}/{pageSize}/{name}")
    public Result<Page> getByPage(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String name){
        //分页构造器
        Page pageInfo = new Page(currentPage, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        if(name.equals("undefined")){
            name = null;
        }
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo, "分页查询成功");
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        Employee emp = employeeService.getById(id);
        return Result.success(emp, "查询成功");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable Long id){
        boolean b = employeeService.removeById(id);
        if(b){
            return Result.success(null, "删除成功");
        }else{
            return Result.error("删除失败");
        }
    }

    /**
     * 更新
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody Employee employee) {
        boolean b = employeeService.updateById(employee);
        if(b){
            return Result.success(null, "更新成功");
        }else{
            return Result.error("更新失败");
        }
    }
}

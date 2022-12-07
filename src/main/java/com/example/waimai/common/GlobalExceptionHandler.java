package com.example.waimai.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) //违反唯一约束抛出的异常
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        String[] splits = ex.getMessage().split(" ");
        String type = splits[2];
        return Result.error(type+"已存在！");
    }

    @ExceptionHandler(CustomException.class) //想删除已关联菜品和套餐的分类时，抛出的业务异常
    public Result<String> cunstomException(CustomException ex){
        log.info(ex.getMessage());
        return Result.error(ex.getMessage());
    }
}

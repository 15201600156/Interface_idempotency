package com.study.exception;

import com.study.R.Result;
import com.study.R.ResultBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyControllerAdvice {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception se) {
        return ResultBuilder.failed(se.getMessage());
    }
}

package com.study.controller;

import com.study.R.Result;
import com.study.R.ResultBuilder;
import com.study.limit.Idempotency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {



    @Idempotency
    @RequestMapping("getUser")
    public Result getUser() {
        return ResultBuilder.success("OK");
    }
}

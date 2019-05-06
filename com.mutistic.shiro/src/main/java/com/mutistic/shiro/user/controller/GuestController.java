package com.mutistic.shiro.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutistic.shiro.core.controller.BaseController;

@RestController
@RequestMapping("/guest")
public class GuestController extends BaseController {

	@GetMapping("/getMessage")
    public Object getMessage() {
        return success("您拥有用户权限，可以获得该接口的信息！");
    }
}

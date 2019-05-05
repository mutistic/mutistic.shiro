package com.xfrj.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xfrj.core.controller.BaseController;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {


	@GetMapping("/getMessage")
    public  Object getMessage() {
        return success("您拥有管理员权限，可以获得该接口的信息！");
    }
    
}

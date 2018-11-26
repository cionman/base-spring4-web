package com.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("auth")
@Controller
public class AuthController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

}
package com.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("auth")
@Controller
public class AuthController {

    @RequestMapping("/login")
    public String login() throws IOException {
        return "login";
    }

}

package com.base.controller;

import com.base.service.auth.AuthUserDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("/auth")
@Controller
public class AuthController {

    @RequestMapping("/login")
    public String login() throws IOException {
        return "login";
    }

    @RequestMapping("/profile")
    public String profile(@AuthenticationPrincipal AuthUserDetail userDetail, Model model){
        model.addAttribute("profile", userDetail.getAccount());
        return "profile";
    }

    @RequestMapping
    public String invalidSession(){
        return "invalidSession";
    }

}

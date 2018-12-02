package com.base.controller;

import com.base.form.AccountCreateForm;
import com.base.form.ExampleForm;
import com.base.mappers.mariadb.ExampleMapper;
import com.base.service.auth.AuthUserDetail;
import com.base.service.example.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/web/example")
public class ExampleController {

    @Autowired
    ExampleService exampleService;

    @RequestMapping("")
    public String example(){

        //  인증 정보에 접근 하는 방법
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = null;
        String role = null;
        if(authentication.getPrincipal() instanceof AuthUserDetail){
            AuthUserDetail userDetail = AuthUserDetail.class.cast(authentication.getPrincipal());
            loginId = userDetail.getAccount().getLoginId();
            role = userDetail.getAccount().getRole();
            System.out.println("현재 로그인 Id는 " + loginId);
            System.out.println("현재 로그인 Role은" + role);
        }


        return "example/example";
    }


    @RequestMapping("/exampleView.do")
    public String exampleView(){
        exampleService.findAllUser();
        return "example/exampleView";
    }

    @RequestMapping(value="/exampleInput.do", method = RequestMethod.GET)
    public String exampleInput(Model model){
        ExampleForm form = new ExampleForm();
        model.addAttribute(form);
        return "example/input";
    }

    @RequestMapping(value="/exampleInput.do", method = RequestMethod.POST)
    public String exampleInputPost(
            @Valid ExampleForm form
            , BindingResult result){
        if(result.hasErrors()){
            return "example/input";
        }
        return "example/output";
    }

    @RequestMapping(value="/exampleInput2.do", method = RequestMethod.GET)
    public String exampleInput2(Model model){
        AccountCreateForm form = new AccountCreateForm();
        model.addAttribute(form);
        return "example/input2";
    }

    @RequestMapping(value="/exampleInput2.do", method = RequestMethod.POST)
    public String exampleInput2Post(
            @Valid AccountCreateForm form
            , BindingResult result){
        if(result.hasErrors()){
            return "example/input2";
        }

        MultipartFile file = form.getImageFile();
        System.out.println("이미지 이름 :" + file.getOriginalFilename());


        return "example/output";
    }

}

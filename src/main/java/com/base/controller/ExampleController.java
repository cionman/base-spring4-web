package com.base.controller;

import com.base.form.AccountCreateForm;
import com.base.form.ExampleForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("example")
public class ExampleController {

    @RequestMapping("")
    public String example(){
        return "example/example";
    }


    @RequestMapping("/exampleView.do")
    public String exampleView(){
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

}

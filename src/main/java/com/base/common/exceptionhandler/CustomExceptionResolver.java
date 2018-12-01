package com.base.common.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomExceptionResolver implements HandlerExceptionResolver {

    private String profile;

    public CustomExceptionResolver(String profile) {
        this.profile = profile;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView model = new ModelAndView("errors/error");
        if (ex instanceof MaxUploadSizeExceededException) {
            model.getModel().put("message", "파일 용량 초과하였습니다.");
        }
        if("dev".equals(profile)){
            StringWriter stringWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(stringWriter));
            model.getModel().put("errorStack", stringWriter.toString());
        }
        return model;
    }
}

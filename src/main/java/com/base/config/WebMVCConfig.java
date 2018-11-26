package com.base.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan("com.base")
public class WebMVCConfig extends WebMvcConfigurerAdapter {
    /**
     * View 리졸버 정의
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        viewResolver.setContentType("text/html; charset=UTF-8");
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setOrder(1);
        viewResolver.setExposedContextBeanNames("prop");
        registry.viewResolver(viewResolver);
    }
}
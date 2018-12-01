package com.base.config;

import com.base.common.interceptor.RequestLogInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebMvc
@ComponentScan("com.base")
@PropertySource("classpath:properties/app.properties")
public class WebMVCConfig extends WebMvcConfigurerAdapter {


    @Value("${activatedProfile}") private String profile;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        Map<String, MediaType> map = new HashMap<String, MediaType>();
        map.put("json" ,MediaType.APPLICATION_JSON);
        configurer.ignoreAcceptHeader(true).defaultContentType(
                MediaType.TEXT_HTML).mediaTypes(map);
    }



    @Bean
    public ViewResolver internalResourceViewResolver(){
        InternalResourceViewResolver internalResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        internalResolver.setContentType("text/html; charset=UTF-8");
        internalResolver.setViewClass(JstlView.class);
        internalResolver.setOrder(0);
        internalResolver.setExposedContextBeanNames("prop");
        return internalResolver;
    }

    @Bean
    public ViewResolver beanNameViewResolver(){
        return new BeanNameViewResolver();
    }

    @Bean
    public View mappingJackson2JsonView(){
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setPrefixJson(false);
        return view;
    }

    @Bean
    public PropertiesFactoryBean prop() throws IOException {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> resourceList = new ArrayList<Resource>();
        Resource[] resources = resolver.getResources("classpath*:properties/*.properties");
        for (Resource resource : resources) {
            resourceList.add(resource);
        }

        bean.setLocations(resourceList.toArray(new Resource[]{}));
        return bean;
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {

        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);

        List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
        resolvers.add(internalResourceViewResolver());
        resolvers.add(beanNameViewResolver());

        resolver.setViewResolvers(resolvers);
        resolver.setOrder(1);

        List<View> viewList = new ArrayList<View>();
        viewList.add(mappingJackson2JsonView());

        resolver.setDefaultViews(viewList);
        return resolver;
    }

    /**
     * message 사용 설정
     * @return
     */
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/messages"); // 클래스 패스 상에 있는 프로퍼티 파일 이름을 확장자를 제외하고 지정
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**") // 요청경로
                .addResourceLocations("classpath:/static/") //실제 리소스 경로
                .resourceChain(!"dev".equals(profile)) // real 프로필에만 캐시를 잡기 위함
                .addResolver(new VersionResourceResolver()
                        .addContentVersionStrategy("/**"));


    }
}
package com.base.config;

import com.base.common.exceptionhandler.CustomExceptionResolver;
import com.base.common.interceptor.RequestLogInterceptor;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
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
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(internalResourceViewResolver());
    }

    @Bean
    public ViewResolver internalResourceViewResolver(){
        InternalResourceViewResolver internalResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        internalResolver.setContentType("text/html; charset=UTF-8");
        internalResolver.setViewClass(JstlView.class);
        internalResolver.setOrder(0);
        internalResolver.setExposedContextBeanNames("prop"); //JSP에서 prop 설정 사용가능하게 한다.
        return internalResolver;
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

    /**
     * Static 리소스(image,css,js) 설정
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**") // 요청경로
                .addResourceLocations("classpath:/static/") //실제 리소스 경로
                .resourceChain(!"dev".equals(profile)) // real 프로필에만 캐시를 잡기 위함
                .addResolver(new VersionResourceResolver()
                        .addContentVersionStrategy("/**"));
    }

    /**
     * 인터셉터등록
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLogInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }

    /**
     * 파일 업로드를 위한 MultipartResolver
     * 파일 용량 제한을 실질적으로 이곳에서 수행하고 에러를 리턴시킨다.
     * @return
     */
    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(5242880);
        return multipartResolver;

    }

    /**
     * HandlerExceptionResolver
     * @return
     */
    @Bean
    HandlerExceptionResolver errorHandler () {
        return new CustomExceptionResolver(profile);
    }

    /**
     * properties 파일이 인식시키기위해 사용
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    /**
     * System 환경 변수 인식
     * @return
     */
    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigInDev() {
        PropertyPlaceholderConfigurer prop =  new PropertyPlaceholderConfigurer();
        prop.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
        return prop;
    }


    /**
     * HttpMessageConverter Json 변환용 빈
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        return new MappingJackson2HttpMessageConverter(
                Jackson2ObjectMapperBuilder.json()
                        .indentOutput(true)
                        .dateFormat(new StdDateFormat()) //Json 출력 날짜 포맷 설정, 기본 yyyy-MM-dd, LocalDate 및 LocalDateTime도 가능
                        .build());
    }

    /**
     * HttpMessageConverter 적용, 현재는 Json 변환 컨버터만 적용
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, mappingJackson2HttpMessageConverter());
    }


}
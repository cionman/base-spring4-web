package com.base.config;

import com.base.common.component.auth.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@PropertySource("classpath:properties/app.properties")
@ComponentScan("com.base")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Value("${auth.loginUrl}")
    private String LOGIN_URL;

    @Value("${auth.logoutUrl}")
    private String LOGOUT_URL;

    @Value("${auth.successUrl}")
    private String SUCCESS_URL;

    /**
     * spirng secuirty 설정
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }


    /**
     * spring security 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.formLogin()
                .loginPage(LOGIN_URL)
                .defaultSuccessUrl(SUCCESS_URL)
                .usernameParameter("loginId")
                .passwordParameter("pwd")
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll();

        http.logout()
                .logoutUrl(LOGOUT_URL)
                .permitAll();

        http.authorizeRequests()
                .anyRequest().authenticated();
    }

    /**
     * spring security 인증을 위한 UserDetailService적용 및 PasswordEncoder
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(new CustomPasswordEncoder());
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


}

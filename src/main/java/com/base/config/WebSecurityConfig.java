package com.base.config;

import com.base.common.component.auth.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.HashMap;
import java.util.Map;

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
        //super.configure(http);
        http.formLogin()
                .loginPage(LOGIN_URL)
                .usernameParameter("loginId")
                .passwordParameter("pwd")
                .defaultSuccessUrl(SUCCESS_URL, true)// 인증 성공 후 이동
                //.failureHandler(customAuthenticationFailureHandler) //인증 실패시 커스텀하게 사용시
                .permitAll();

        http.logout()
                .logoutUrl(LOGOUT_URL)
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/example/exampleInput.do").hasAuthority("EXAM_USER") //hasRole이 되지 않는다. UserDetailsService 구현으로 Authorities가 동작하는듯
                .anyRequest().authenticated();

        http.exceptionHandling()
                .accessDeniedPage("/WEB-INF/views/accessDeniedError.jsp");

        http.sessionManagement()
                .invalidSessionUrl("/auth/invalidSession")
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // RestApI 사용시 Session 사용하지 않음 처리
                .sessionFixation().newSession(); // 세션 고정공격 방지

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

package com.base.config;

import com.base.common.component.auth.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:properties/app.properties")
@ComponentScan("com.base")
public class SecurityConfig {

    /**
     * /web/** 경로 Spring Security설정
     */
    @Configuration
    @Order(1)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {


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
            web.ignoring().antMatchers("/static/**");
        }


        /**
         * spring security 설정
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.antMatcher("/web/**");

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
                    .antMatchers("/web/example/exampleInput.do").hasRole("EXAM") // hasAuthority("ROLE_EXAM") == hasRole("EXAM") Role은 앞에 'ROLE_'서두가 빠진 것이 ROLE이 된다.
                    .anyRequest().authenticated();

            http.exceptionHandling()
                    .accessDeniedPage("/WEB-INF/views/accessDeniedError.jsp");

            http.sessionManagement()
                    .invalidSessionUrl("/web/auth/invalidSession")
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

    }


    /**
     * /api/** 경로 Spring Security설정
     */
    @Configuration
    @Order(2)
    public static class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
        /**
         * spirng secuirty 설정
         * @param web
         * @throws Exception
         */
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/static/**");
        }


        /**
         * spring security 설정
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.antMatcher("/api/**");

            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // RestApI 사용시 Session 사용하지 않음 처리
                    .sessionFixation().newSession(); // 세션 고정공격 방지
        }
    }

}





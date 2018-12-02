package com.base;

import com.base.config.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {SecurityConfig.class}
)
public class WebSecurityBasicTest extends BasicControllerTest {

    @Value("${auth.loginUrl}")
    private String LOGIN_URL;

    @Value("${auth.logoutUrl}")
    private String LOGOUT_URL;


    @Test
    public void testFormLogin() throws Exception{
        this.mockMvc.perform(
                    formLogin()
                    .loginProcessingUrl(LOGIN_URL)
                    .user("loginId","test")
                    .password("pwd","qwer1234"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/web/example"))
                .andExpect(authenticated().withRoles("EXAM")); //Authority에서 ROLE_ 이제거된 것이 ROLE이 된다.
    }


    @WithMockUser(username = "test", roles = "EXAM") //인증된 상태로 설정
    @Test
    public void testLogout() throws Exception{
        mockMvc.perform(logout(LOGOUT_URL))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/web/auth/login?logout"))
                .andExpect(unauthenticated());
    }

}

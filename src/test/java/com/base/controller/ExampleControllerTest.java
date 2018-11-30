package com.base.controller;

import com.base.BasicControllerTest;
import com.base.config.WebMVCConfig;
import com.base.config.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.nio.file.AccessDeniedException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {WebSecurityConfig.class, WebMVCConfig.class}
)
public class ExampleControllerTest extends BasicControllerTest {

    @WithMockUser(username = "test", roles="USER")
    @Test
    public void testExampleInputWithWrongRoles() throws Exception {

        mockMvc.perform(get("/example/exampleInput.do"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/WEB-INF/views/accessDeniedError.jsp"))
                .andReturn();

    }
}

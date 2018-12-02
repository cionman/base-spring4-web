package com.base.controller;

import com.base.BasicControllerTest;
import com.base.config.SecurityConfig;
import com.base.config.WebMVCConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {SecurityConfig.class, WebMVCConfig.class}
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


    @WithMockUser(username = "test", roles="EXAM")
    @Test
    public void testExampleInputWithRightRoles() throws Exception {

        mockMvc.perform(get("/example/exampleInput.do"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @WithMockUser(username = "test", roles="EXAM")
    @Test
    public void testPostExampleInput() throws Exception {

        mockMvc.perform(post("/example/exampleInput.do")
                        .param("text", "abcdefg")
                        .with(csrf()))

                .andDo(print())
                .andExpect(view().name("example/output"))
                .andExpect(status().isOk())
                .andReturn();

    }

}

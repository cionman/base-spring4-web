package com.base.common.util;

import com.base.config.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {SecurityConfig.class}
)
public class LogUtilTest {

    @Autowired
    LogUtil logUtil;

    @Test
    public void testErrorLog() throws Exception{
        logUtil.error("msg 테스트", new Exception("Exception 메세지"));
    }
}

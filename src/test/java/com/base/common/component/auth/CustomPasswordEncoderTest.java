package com.base.common.component.auth;


import com.base.common.component.auth.CustomPasswordEncoder;
import org.junit.Test;

public class CustomPasswordEncoderTest {

    @Test
    public void testCustomPasswordEncoder() throws Exception{
        CustomPasswordEncoder enc = new CustomPasswordEncoder();
        System.out.println("암호 ::: " + enc.encode("qwer1234"));
    }
}

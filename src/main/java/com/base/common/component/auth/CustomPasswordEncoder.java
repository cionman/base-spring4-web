package com.base.common.component.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;

/**
 * 스프링 제공 BCryptPasswordEncoder를 살짝 변형해서 사용하기 위한 클래스
 */
public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    private final String CUSTOM_SALT = "ABC!2ASD3QW";

    public CustomPasswordEncoder() throws NoSuchAlgorithmException {
        super();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword + CUSTOM_SALT);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(rawPassword +CUSTOM_SALT, encodedPassword);
    }
}

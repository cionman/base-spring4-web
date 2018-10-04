package com.base.common.component;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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

package com.base.common.component.auth;

import com.base.common.util.LogUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증 실패 시 커스텀한 동작을 설정할 때 구현하고 WebSecurityConfig configure 메소드에서 .failureHandler를 등록한다.
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    LogUtil logUtil;

    @Value("${auth.loginUrl}")
    private String LOGIN_URL;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logUtil.info(request.getRemoteAddr() + e.getMessage());
        request.setAttribute("errorMsg", "로그인에 실패하였습니다.");
        request.getRequestDispatcher(MessageFormat.format("{0}{1}", request.getContextPath(), LOGIN_URL))
                .forward(request, response);
    }
}

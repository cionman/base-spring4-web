package com.base.common.component.auth;

import com.base.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * 인증 이벤트를 캐치하는 리스너
 */
@Component
public class AuthenticationEventListeners {
    @Autowired
    LogUtil logUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * Authentication 성공 시 이벤트 발생
     * @param event
     */
    @EventListener
    public void handleSuccessEvent(InteractiveAuthenticationSuccessEvent event){

    }


    /**
     * BadCredential Exception 발생시 이벤트 발생
     * @param event
     */
    @EventListener
    public void handleBadCredentials(AuthenticationFailureBadCredentialsEvent event){
        logUtil.info(MessageFormat.format("[로그인 실패][{0}][{1}]", request.getRemoteAddr(), event.getException().getMessage()));
    }

}

package com.base.service.auth;

import com.base.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

public class AuthUserDetail extends User {

    private final Account account;


    public AuthUserDetail(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getLoginId(), account.getPwd(), account.isEnabled()
                , true, true, true, authorities);
        this.account = account;
    }

    public Account getAccount(){
        return account;
    }
}

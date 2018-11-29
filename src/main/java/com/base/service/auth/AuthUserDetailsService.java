package com.base.service.auth;

import com.base.mappers.mariadb.AuthMapper;
import com.base.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    AuthMapper authMapper;

    @Transactional(value = "mariaDBTransactionManager", readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Account account = Optional
                            .ofNullable(authMapper.findUserByLoginParam(loginId))
                            .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new AuthUserDetail(account, getAuthorities(account));
    }

    private Collection<GrantedAuthority> getAuthorities(Account account) {
        return AuthorityUtils.createAuthorityList(account.getRole());
    }
}

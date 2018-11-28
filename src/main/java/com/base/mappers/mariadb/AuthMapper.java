package com.base.mappers.mariadb;

import com.base.model.Account;
import org.springframework.stereotype.Component;

@Component
public interface AuthMapper {
    Account findUserByLoginParam(String loginId);
}

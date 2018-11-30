package com.base.mappers.mariadb;

import com.base.model.UserVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExampleMapper {
    @PreAuthorize("hasRole('EXAM')")
    int findAllUserCount();
    List<UserVO> findAllUser();
}

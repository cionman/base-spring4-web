package com.base.mappers.mariadb;

import com.base.model.UserVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExampleMapper {
    int findAllUserCount();
    List<UserVO> findAllUser();
}

package com.base.service.example;

import com.base.model.UserVO;

import java.util.List;

public interface ExampleService {

    int findAllUserCount();

    List<UserVO> findAllUser();
}

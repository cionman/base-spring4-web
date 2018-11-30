package com.base.service.example;

import com.base.mappers.mariadb.ExampleMapper;
import com.base.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleServiceImpl implements ExampleService {

    @Autowired
    ExampleMapper exampleMapper;


    @Override
    public int findAllUserCount() {
        return exampleMapper.findAllUserCount();
    }

    @PreAuthorize("hasRole('EXAM')")
    @Override
    public List<UserVO> findAllUser() {
        return exampleMapper.findAllUser();
    }
}

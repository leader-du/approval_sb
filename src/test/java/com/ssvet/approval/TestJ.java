package com.ssvet.approval;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.mapper.UserMapper;
import com.ssvet.approval.service.IUserService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @author Programmer_Liu.
 * @since 2020/8/29 10:02
 */
public class TestJ {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Test
    public void bCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
//        System.out.println(passwordEncoder.encode("123"));
    }

    @Test
    public void test(){


        userService.getUserByChname("王磊");

    }

}

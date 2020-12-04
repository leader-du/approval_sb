package com.ssvet.approval;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Note;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.mapper.*;
import com.ssvet.approval.service.IUserService;
import com.ssvet.approval.utils.resp.CommonResult;
import com.ssvet.approval.utils.upload.GetPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Programmer_Liu.
 * @since 2020/8/29 10:02
 */
public class TestJ {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private NoteMapper noteMapper;

    @Test
    public void bCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
//        System.out.println(passwordEncoder.encode("123"));
    }

    @Test
    public void test(){


    }

}

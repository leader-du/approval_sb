package com.ssvet.approval.controller;

import com.ssvet.approval.entity.Dept;
import com.ssvet.approval.service.IDeptService;
import com.ssvet.approval.utils.resp.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门控制器
 */
@Slf4j
@RestController
@RequestMapping("/approval/dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @RequestMapping("/getDeptList")
    public CommonResult getDeptList(){

        return deptService.getDeptList();

    }

    @RequestMapping("/addDept")
    public CommonResult addDept(@RequestBody Dept dept){

        System.out.println(dept);

        return deptService.addDept(dept);

    }

    @RequestMapping("/getDeptListByDname")
    public CommonResult getDeptListByDname(@RequestBody Dept dept){


        return deptService.getDeptListByDname(dept.getDname());

    }

    @RequestMapping("/delDeptById")
    public CommonResult delDeptById(@RequestBody Dept dept){


        return deptService.delDeptById(dept.getDid());

    }

    @RequestMapping("/updateDept")
    public CommonResult updateDept(@RequestBody Dept dept){


        return deptService.updateDept(dept);

    }

}

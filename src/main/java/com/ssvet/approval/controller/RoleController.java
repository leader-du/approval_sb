package com.ssvet.approval.controller;


import com.ssvet.approval.service.IRoleService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@RestController
@RequestMapping("/approval/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("/getRoleList")
    public CommonResult getRoleList(){

        return roleService.getRoleList();

    }

}

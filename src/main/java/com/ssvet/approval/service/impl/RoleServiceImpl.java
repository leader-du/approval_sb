package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.Role;
import com.ssvet.approval.mapper.RoleMapper;
import com.ssvet.approval.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Service
public class RoleServiceImpl  implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public CommonResult getRoleList() {
        // 排除管理员
        List<Role> roles = roleMapper.selectList(new QueryWrapper<Role>().notIn("role_id",21));

        return CommonResult.success(roles);
    }
}

package com.ssvet.approval.service;

import com.ssvet.approval.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ssvet.approval.utils.resp.CommonResult;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface IRoleService{
    /**
     * 获取所有职位列表
     * @return
     */
    CommonResult getRoleList();

}

package com.ssvet.approval.service;

import com.ssvet.approval.entity.Dept;
import com.ssvet.approval.utils.resp.CommonResult;


public interface IDeptService {

    /**
     * 获取部门列表
     * @return
     */

    CommonResult getDeptList();

    /**
     * 添加部门
     * @param dept
     * @return
     */
    CommonResult addDept(Dept dept);

    /**
     * 通过部门名称查部门信息
     * @param dname
     * @return
     */
    CommonResult getDeptListByDname(String dname);

    /**
     * 通过id删除部门
     * @param id
     * @return
     */
    CommonResult delDeptById(int id);

    /**
     * 更新部门信息
     * @param dept
     * @return
     */
    CommonResult updateDept(Dept dept);

}

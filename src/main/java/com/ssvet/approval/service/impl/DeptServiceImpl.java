package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.Dept;
import com.ssvet.approval.mapper.AreaMapper;
import com.ssvet.approval.mapper.DeptMapper;
import com.ssvet.approval.service.IDeptService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeptServiceImpl implements IDeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public CommonResult getDeptList() {

        List<Dept> depts = deptMapper.selectList(new QueryWrapper<Dept>());

        return CommonResult.success(depts);
    }

    @Override
    @Transactional
    public CommonResult addDept(Dept dept) {

        int n = deptMapper.insert(dept);

        if(n == 0) return CommonResult.failed("添加失败");

        return CommonResult.success("添加成功");
    }

    @Override
    public CommonResult getDeptListByDname(String dname) {

        List<Dept> list = deptMapper.selectList(new QueryWrapper<Dept>().like("dname", dname));

        for (Dept dept : list) {
            if(dept.getDAreaId() != null) {

                dept.setArea(areaMapper.selectById(dept.getDAreaId()));

            }

        }

        return CommonResult.success(list);
    }

    @Override
    @Transactional
    public CommonResult delDeptById(int id) {

        int i = deptMapper.deleteById(id);

        return i != 0 ? CommonResult.success("删除成功") : CommonResult.success("删除失败");
    }

    @Override
    @Transactional
    public CommonResult updateDept(Dept dept) {

        deptMapper.updateById(dept);

        return CommonResult.success("更新成功");
    }
}

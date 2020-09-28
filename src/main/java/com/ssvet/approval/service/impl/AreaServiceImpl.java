package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.Area;
import com.ssvet.approval.mapper.AreaMapper;
import com.ssvet.approval.service.IAreaService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements IAreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Override
    public Area getAreaByUid(Integer uid) {
        return areaMapper.getAreaByUid(uid);
    }

    @Override
    public CommonResult getAreaList() {

        return CommonResult.success(areaMapper.selectList(new QueryWrapper<>()));
    }
}

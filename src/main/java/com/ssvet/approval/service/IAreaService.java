package com.ssvet.approval.service;

import com.ssvet.approval.entity.Area;
import com.ssvet.approval.utils.resp.CommonResult;

import java.util.List;

public interface IAreaService {

    /**
     * 通过总经理ID查出基地归属区域
     * @param uid
     * @return
     */
    Area getAreaByUid(Integer uid);

    /**
     * 获取大区列表
     * @return
     */
   CommonResult getAreaList();
}

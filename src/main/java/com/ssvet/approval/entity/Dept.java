package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dept")
public class Dept {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @TableId(value = "did", type = IdType.AUTO)
    private Integer did;

    /**
     * 部门名称
     */
    private String dname;

    /**
     * 关联区域ID
     */
    //@TableField("d_area_id")
    private Integer dAreaId;

    @TableField(exist = false)
    private Area area;

}

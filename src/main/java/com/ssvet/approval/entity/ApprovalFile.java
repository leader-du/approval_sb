package com.ssvet.approval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("approval_file")
public class ApprovalFile {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    @TableId(value = "approval_file_id", type = IdType.AUTO)
    private Integer approvalFileId;

    /**
     * 文件的路径
     */

    private String approvalFileUrl;

    /**
     * 上传文件名
     */

    private String approvalFileName;



    /**
     * 关联时间ID
     */

    private Integer eventId;

}

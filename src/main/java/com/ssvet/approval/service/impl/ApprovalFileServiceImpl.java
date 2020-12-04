package com.ssvet.approval.service.impl;

import com.ssvet.approval.entity.ApprovalFile;
import com.ssvet.approval.entity.Note;
import com.ssvet.approval.service.IApprovalFileService;
import com.ssvet.approval.utils.resp.CommonResult;
import com.ssvet.approval.utils.upload.GetPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class ApprovalFileServiceImpl implements IApprovalFileService {
    @Override
    public CommonResult uploadFile(MultipartFile file) {
        // 设置文件上传路径,没有目录就创建

        String uploadPath = GetPath.getUploadPath("static/upload");

        String name = System.currentTimeMillis() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        File uploadFile = new File(uploadPath + File.separator + name);

        try {

            file.transferTo(uploadFile);
            String fileUrl = "upload/"+name;
            ApprovalFile approvalFile = new ApprovalFile();
            approvalFile.setApprovalFileUrl(fileUrl);
            return CommonResult.success(approvalFile, "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CommonResult.failed("文件上传失败");
    }
}

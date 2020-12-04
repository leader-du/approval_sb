package com.ssvet.approval.controller;

import com.ssvet.approval.service.IApprovalFileService;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/approval/file")
public class ApprovalFileController {

    @Autowired
    private IApprovalFileService approvalFileService;

    @RequestMapping("/uploadFile")
    CommonResult uploadFile(MultipartFile file){

        return approvalFileService.uploadFile(file);

    }

}

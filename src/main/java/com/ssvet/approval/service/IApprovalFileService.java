package com.ssvet.approval.service;

import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.web.multipart.MultipartFile;

public interface IApprovalFileService {

    CommonResult uploadFile(MultipartFile file);
}

package com.ssvet.approval.service;

import com.ssvet.approval.entity.User;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
public interface IUserService extends UserDetailsService {
    /**
     * 修改密码
     * @param user
     * @return
     */
    CommonResult updatePassword(User user);

    /**
     * 获取审批人列表
     * @param user 上一个审批人信息
     * @return
     */
    CommonResult getApprovalList(User user);

    /**
     * 通过区域总经理ID查询对应基地总经理信息
     * @param uid
     * @return
     */

    List<User> getBaseManager(Integer uid);

    /**
     * 通过总经理ID查出副总信息
     * @param uid
     * @return
     */

    User getFuManager(Integer uid);

    /**
     * 通过用户ID查询用户
     * @param uid
     * @return
     */

    User getUserByUid(Integer uid);

    /**
     * 添加用户
     * @param user
     * @return
     */

    CommonResult addUser(User user);

    /**
     * 检查用户名是否存在
     * @param user
     * @return
     */
    CommonResult checkUserIsExist(User user);

    /**
     * 通过姓名查询用户
     * @param chName
     * @return
     */
    CommonResult getUserByChname(String chName);

    /**
     * 通过id删除用户
     * @param id
     * @return
     */
    CommonResult delUserById(Integer id);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    CommonResult updateUser(User user);

    /**
     * 添加管理员
     * @param user
     * @return
     */
    CommonResult addAdmin(User user);

    /**
     * 获取管理员列表
     * @return
     */
    CommonResult getAdminList();

    /**
     * 更新管理员
     * @param user
     * @return
     */
    CommonResult updateAdmin(User user);

    /**
     * 删除管理员
     * @param id
     * @return
     */
    CommonResult deleteAdmin(int id);
}

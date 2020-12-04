package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.*;
import com.ssvet.approval.mapper.*;
import com.ssvet.approval.service.IUserService;
import com.ssvet.approval.utils.resp.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public CommonResult updatePassword(User user) {
        if (StringUtils.isEmpty(user.getPassword())) {
            return CommonResult.failed("新密码不能为空");
        }
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isEmpty(userDetails) || StringUtils.isEmpty(userDetails.getId())) {
            log.error("认证信息的用户信息为空");
            return CommonResult.validateFailed("服务器异常，请重新登陆再试或者联系管理员");
        }
        User userOld = userMapper.selectById(userDetails.getId());
        if (passwordEncoder.matches(user.getPassword(), userOld.getPassword())) {
            return CommonResult.failed("新旧密码不能相同");
        }
        user.setId(userDetails.getId());
        user.setVersion(userOld.getVersion());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.updateById(user);
        return CommonResult.success("密码修改成功");
    }

    @Override
    public CommonResult getApprovalList(User user) {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (StringUtils.isEmpty(userDetails) || StringUtils.isEmpty(userDetails.getId())) {
            log.error("认证信息的用户信息为空");
            return CommonResult.validateFailed("服务器异常，请重新登陆再试或者联系管理员");
        }
        //审批流程必然为先出纳级别为1，然后会计级别为2，然后其他
        List<User> users = null;

        //如果审批人为空，说明选择第一个审批人
        if (user.getSelectCount() == 1) {
            //第一个审批必然是出纳，即角色级别为1
            //查出所有角色级别为1的所有用户，但不包括自己
            Integer roleGrade = 1;

            Integer currentUserId = userDetails.getId();

            String chName = user.getChName();

            //用户名模糊查询
            users = userMapper.selectListByRoleGrade(currentUserId,chName,roleGrade);

        } else if (user.getSelectCount() == 2) {

            //说明第一个审批人是出纳，那么第二个审批人必然是会计
            //查询出所有会计,会计的角色级别为2，不包括自己
            users = userMapper.selectListByRoleGrade(userDetails.getId(), user.getChName(), 2);

        }else{

            // 获取当前用户的最大权限等级

            Integer userMaxRoleGrade = getUserMaxRoleGrade(userDetails);

            //获取上一个审批人roleGrade,并找出最大权限等级

            List<Role> roles = userMapper.getRolesByUid(user.getPreApprovalId());

            Integer prevUserRoleGrade = getUserMaxRoleGrade(roles);


            //查询当前登陆人最高级别
            if (userMaxRoleGrade == -1) {
                return CommonResult.failed("上一个审批人还未分配角色，请联系管理员");
            } else {
                //审批人的用户级别比当前用户级别要高  还要高于上一个审批人
                users = userMapper.selectLeaderList(user.getChName(),userMaxRoleGrade, prevUserRoleGrade);
            }

            if (user == null) {

                return CommonResult.validateFailed("服务器异常，请稍后再试或者联系管理员");
            }

        }

        // 为审批人添加部门信息

        for (User u : users) {

            u.setDept(deptMapper.selectById(u.getDeptId()));

        }

        return CommonResult.success(users);

    }

    /**
     * 根据区域经理ID查出所负责所有基地的总经理信息
     * @param uid
     * @return
     */

    @Override
    public List<User> getBaseManager(Integer uid) {

        List<User> list = userMapper.getBaseManager(uid);

        for (User user : list) {

            user.setDept(deptMapper.selectById(user.getDeptId()));

        }

        return list;
    }

    @Override
    public User getFuManager(Integer uid) {

        List<User> list = userMapper.getFuManager(uid);

        if(list.size() > 0){

            User user = list.get(0);

            user.setDept(deptMapper.selectById(user.getDeptId()));

            return user;
        }

        return null;


    }

    @Override
    public User getUserByUid(Integer uid) {

        User user = userMapper.selectById(uid);

        user.setDept(deptMapper.selectById(user.getDeptId()));

        return user ;
    }

    @Override
    @Transactional
    public CommonResult addUser(User user) {

        String password = passwordEncoder.encode(user.getPassword());

        user.setPassword(password);

        userMapper.insert(user);

        UserRole userRole = new UserRole();

        userRole.setUrUserId(user.getId());

        userRole.setUrRoleId(user.getRoles().get(0).getRoleId());

        userRoleMapper.insert(userRole);

        return CommonResult.success(true,"添加成功");
    }

    @Override
    public CommonResult checkUserIsExist(User user) {

        Integer n = userMapper.selectCount(new QueryWrapper<User>().eq("uname", user.getUName()));

        return n > 0 ? CommonResult.success(true) : CommonResult.success(false);
    }

    @Override
    public CommonResult getUserByChname(String chName) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();

        wrapper.eq("ch_name",chName).notIn("dept_id",42); //排除掉管理员

        List<User> users = userMapper.selectList(wrapper);

        for (User user : users) {

            user.setDept(deptMapper.selectById(user.getDeptId()));

            List<UserRole> urList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("ur_user_id", user.getId()));

            List<Role> roles = new ArrayList<>();

            for (UserRole userRole : urList) {

                roles.add(roleMapper.selectById(userRole.getUrRoleId()));

            }

            user.setRoles(roles);

        }

        System.out.println(users);

        return CommonResult.success(users);
    }

    @Override
    @Transactional
    public CommonResult delUserById(Integer id) {

        /*
            删除用户要删除外键关联的相关数据
         */

        // 删除用户权限中间表中数据

        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("ur_user_id",id));

        // 获取相关联的审批事件

        List<Event> eventList = eventMapper.selectList(new QueryWrapper<Event>().eq("approval_user_id", id));


        if(eventList.size() > 0){

            for (Event event : eventList) {

                //根据事件删除相关联外键约束数据

                //删除审批记录

                recordMapper.delete(new QueryWrapper<Record>().eq("event_id",event.getApprovalEventId()));

                // 删除相关的凭证记录

                noteMapper.delete(new QueryWrapper<Note>().eq("note_event_id",event.getApprovalEventId()));

            }

            // 删除审批事件记录

            eventMapper.delete(new QueryWrapper<Event>().eq("approval_user_id",id));

        }

        //删除用户

        int i = userMapper.deleteById(id);

        return i > 0 ? CommonResult.success(true) : CommonResult.success(false);
    }

    @Override
    @Transactional
    public CommonResult updateUser(User user) {

        User oldUser = userMapper.selectById(user.getId());

        if(!oldUser.getPassword().equals(user.getPassword())){

            String pw = passwordEncoder.encode(user.getPassword());

            user.setPassword(pw);

        }

        userMapper.updateById(user);

        return CommonResult.success("更新成功");
    }

    @Override
    @Transactional
    public CommonResult addAdmin(User user) {
        //设置部门
        user.setDeptId(42);

        user.setStatus(4);

        // 密码加密

        String password = passwordEncoder.encode(user.getPassword());

        user.setPassword(password);

        userMapper.insert(user);
        //用户 角色中间表数据
        UserRole userRole = new UserRole();

        userRole.setUrUserId(user.getId());

        userRole.setUrRoleId(21);

        userRoleMapper.insert(userRole);

        return CommonResult.success("添加成功");
    }

    @Override
    public CommonResult getAdminList() {

        List<User> list = userMapper.selectList(new QueryWrapper<User>().eq("status", 4));

        return CommonResult.success(list);
    }

    @Override
    public CommonResult updateAdmin(User user) {

        User oldUser = userMapper.selectById(user.getId());

        if(!oldUser.getPassword().equals(user.getPassword())){

            String pw = passwordEncoder.encode(user.getPassword());

            user.setPassword(pw);

        }

        userMapper.updateById(user);

        return CommonResult.success("更新成功");
    }

    @Override
    @Transactional
    public CommonResult deleteAdmin(int id) {

        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("ur_user_id",id));

        userMapper.deleteById(id);

        return CommonResult.success("删除成功");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uname", username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Role> userAllRoles = roleMapper.getUserAllRoles(user.getId());
        user.setRoles(userAllRoles);
        return user;
    }

    /**
     * 获取员工的最大角色级别
     *
     * @param user
     * @return
     */
    private Integer getUserMaxRoleGrade(User user) {
        List<Role> roles = userMapper.getRoles(user);
        if (roles == null || roles.size() <= 0) {
            return -1;
        }
        Integer roleGrade = roles.stream().max(Comparator.comparing(Role::getRoleGrade)).get().getRoleGrade();
        return roleGrade;
    }

    private Integer getUserMaxRoleGrade(List<Role> roles){

        Integer userMaxRoleGrade = 0;

        for (Role role : roles) {

            if(role.getRoleGrade() > userMaxRoleGrade){

                userMaxRoleGrade = role.getRoleGrade();

            }

        }

        return userMaxRoleGrade;

    }
}

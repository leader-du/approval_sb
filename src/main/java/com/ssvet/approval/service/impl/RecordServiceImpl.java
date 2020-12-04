package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ssvet.approval.entity.Event;
import com.ssvet.approval.entity.Note;
import com.ssvet.approval.entity.Record;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.mapper.*;
import com.ssvet.approval.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssvet.approval.utils.resp.CommonResult;
import com.ssvet.approval.utils.upload.GetPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 审批记录表 服务实现类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Service
public class RecordServiceImpl  implements IRecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public CommonResult addRecord(Record record) {

        //审批记录表中插入数据

        record.setApprovalDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        String remark = record.getApprovalUserRemark();

        if(record.getStatus() == 1){

            if("".equals(remark) || remark == null){

                record.setApprovalUserRemark("同意");

            }

        }

        recordMapper.insert(record);

        //更新审批事件表中数据

        //获取对应审批事件

        Event event = eventMapper.selectById(record.getEventId());

        Integer status = record.getStatus();
        //设置事件审批状态
        if(status == 0){

            event.setStatus(3);

        }

        // 设置当前审批人

        Integer approvalUserIndex = event.getApprovalUserIndex();

        String[] approvalPersons = event.getApprovalPersons().split(",");

        // 判断是否到了最后一个审批人
        if(status == 1 && approvalUserIndex == approvalPersons.length - 1){

            event.setStatus(4);

        }

        if(approvalUserIndex < approvalPersons.length - 1) {

            approvalUserIndex++;

            Integer nowApprovalId = Integer.parseInt(approvalPersons[approvalUserIndex]);

            event.setNowApprovalId(nowApprovalId);

            event.setApprovalUserIndex(approvalUserIndex);

            if(status == 1){

                event.setStatus(2);

            }

        }


        // 更新event表

        eventMapper.updateById(event);

        return CommonResult.success("审批完成");
    }

    @Override
    @Transactional
    public CommonResult getRecord(Event event) {

        //通过事件ID查询出审批记录，并按照审批事件升序排序
        List<Record> records = recordMapper.selectList(new QueryWrapper<Record>().eq("event_id", event.getApprovalEventId()).orderByAsc("approval_date"));

        for (Record record : records) {

            record.setUser(userMapper.selectById(record.getApprovalLeaderId()));
        }

        //通过事件id查询出事件

        Event e = eventMapper.selectById(event.getApprovalEventId());

        e.setRecords(records);

        //List<User> users = userMapper.selectList(new QueryWrapper<User>().in("uid", e.getApprovalPersons().split(",")));

        List<User> users = new ArrayList<>();

        // 必须保证审批顺序，如果用in顺序会乱，所以单独处理

        for (String s : e.getApprovalPersons().split(",")) {

            users.add(userMapper.selectById(s));

        }

        e.setUsers(users);

        // 发起审批人的信息封装进event

        User user = userMapper.selectById(e.getApprovalUserId());

        user.setDept(deptMapper.selectById(user.getDeptId()));

        e.setUser(user);


        return CommonResult.success(e);
    }

    @Override
    @Transactional
    public CommonResult adminDeleteRecord(int days) {

        // 查询距离现在days天的记录

        List<Event> events = eventMapper.getCompletedApprovalListByDays(days);

        // 删除凭证 先删除服务器上的图片
        //获取要删除文件的名字列表
        List<String> urls = new ArrayList<>();

        for (Event event : events) {

            List<Note> notes = noteMapper.selectList(new QueryWrapper<Note>().eq("note_event_id", event.getApprovalEventId()));

            for (Note note : notes) {

                String url = note.getApprovalNoteUrl();

                url = url.substring(url.lastIndexOf("/")+1);

                urls.add(url);

            }

            // 删除数据库中凭证信息

            noteMapper.delete(new QueryWrapper<Note>().eq("note_event_id",event.getApprovalEventId()));

            // 删除审批记录信息

            recordMapper.delete(new QueryWrapper<Record>().eq("event_id",event.getApprovalEventId()));


            // 删除审批事件

            eventMapper.deleteById(event.getApprovalEventId());

        }

        // 删除服务器上的凭证信息

        String uploadPath = GetPath.getUploadPath("static/upload");

        File parent = new File(uploadPath);

        File[] files = parent.listFiles();

        for (String url : urls) {

            for (File file : files) {

                if(url.equals(file.getName())){

                    file.delete();

                    break;

                }
            }

        }

        return CommonResult.success("删除成功");
    }
}

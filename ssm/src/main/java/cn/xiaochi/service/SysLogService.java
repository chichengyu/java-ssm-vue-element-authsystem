package cn.xiaochi.service;

import cn.xiaochi.beans.LogType;
import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysLogDao;
import cn.xiaochi.model.SysAcl;
import cn.xiaochi.model.SysAclModule;
import cn.xiaochi.model.SysDept;
import cn.xiaochi.model.SysLogWithBLOBs;
import cn.xiaochi.model.SysRole;
import cn.xiaochi.model.SysRoleAcl;
import cn.xiaochi.model.SysRoleUser;
import cn.xiaochi.model.SysUser;
import cn.xiaochi.util.IpUtil;
import cn.xiaochi.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 记录权限操作日志
 */
@Service
@Slf4j
public class SysLogService {

    @Resource
    private SysLogDao sysLogDao;

    /**
     * 部门日志记录
     * @param before
     * @param after
     */
    public void saveDeptLog(SysDept before,SysDept after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId():after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }

    /**
     * 用户日志记录
     * @param before
     * @param after
     */
    public void saveUserLog(SysUser before, SysUser after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(after == null ? before.getId():after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }

    /**
     * 权限模块日志记录
     * @param before
     * @param after
     */
    public void saveAclModuleLog(SysAclModule before,SysAclModule after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId():after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }

    /**
     * 权限点日志记录
     * @param before
     * @param after
     */
    public void saveAclLog(SysAcl before, SysAcl after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(after == null ? before.getId():after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }

    /**
     * 角色日志记录
     * @param before
     * @param after
     */
    public void saveRoleLog(SysRole before, SysRole after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId():after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }

    /**
     * 角色权限日志记录
     * @param roleId
     * @param before
     * @param after
     */
    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }

    /**
     * 角色用户日志记录
     * @param roleId
     * @param before
     * @param after
     */
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after){
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);

        sysLogDao.insertSelective(sysLog);
    }
}

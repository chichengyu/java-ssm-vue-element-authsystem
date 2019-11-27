package cn.xiaochi.service;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysAclDao;
import cn.xiaochi.exception.ParamException;
import cn.xiaochi.model.SysAcl;
import cn.xiaochi.param.AclParam;
import cn.xiaochi.util.IpUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SysAclSerivice {

    @Resource
    private SysAclDao sysAclDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 添加权限点
     * @param aclParam
     */
    public void save(AclParam aclParam) throws ParamException {
        if (checkExist(aclParam.getAclModuleId(),aclParam.getName(),aclParam.getId())){
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId()).url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());

        sysAclDao.insertSelective(acl);

        // 记录日志
        sysLogService.saveAclLog(null,acl);
    }

    /**
     * 更新权限点
     * @param aclParam
     */
    public void update(AclParam aclParam) throws ParamException {
        if (checkExist(aclParam.getAclModuleId(),aclParam.getName(),aclParam.getId())){
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclDao.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(before,"待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(aclParam.getId()).name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId()).url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysAclDao.updateByPrimaryKeySelective(after);

        // 记录日志
        sysLogService.saveAclLog(before,after);
    }

    /**
     * 获取权限码
     * @return
     */
    private String generateCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)Math.random()*100;
    }

    /**
     * 判断同模块下权限点名称是否存在
     * @param aclModuleId
     * @param id
     * @param name
     * @return
     */
    private Boolean checkExist(Integer aclModuleId,String name,Integer id){
        return sysAclDao.countNyAclModuleId(aclModuleId,name,id) > 0;
    }
}

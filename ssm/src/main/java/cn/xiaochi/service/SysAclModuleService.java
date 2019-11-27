package cn.xiaochi.service;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysAclModuleDao;
import cn.xiaochi.exception.ParamException;
import cn.xiaochi.model.SysAclModule;
import cn.xiaochi.model.SysDept;
import cn.xiaochi.param.AclModuleParam;
import cn.xiaochi.util.IpUtil;
import cn.xiaochi.util.LevelUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysAclModuleService {

    @Resource
    private SysAclModuleDao sysAclModuleDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 添加权限模块
     * @param aclModuleParam
     */
    public void save(AclModuleParam aclModuleParam) throws ParamException {
        if(checkExist(aclModuleParam.getParentId(),aclModuleParam.getName(),aclModuleParam.getId())){
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder().name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId()).seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()),aclModuleParam.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());

        sysAclModuleDao.insertSelective(aclModule);

        // 记录日志
        sysLogService.saveAclModuleLog(null,aclModule);
    }

    /**
     * 更新权限模块
     * @param aclModuleParam
     */
    public void update(AclModuleParam aclModuleParam) throws ParamException {
        if(checkExist(aclModuleParam.getParentId(),aclModuleParam.getName(),aclModuleParam.getId())){
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule before = sysAclModuleDao.selectByPrimaryKey(aclModuleParam.getId());
        Preconditions.checkNotNull(before,"待更新的权限模块不存在");

        SysAclModule after = SysAclModule.builder().id(aclModuleParam.getId()).name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId()).seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(aclModuleParam.getParentId()),aclModuleParam.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        // 更新
        updateWithChild(before,after);

        // 记录日志
        sysLogService.saveAclModuleLog(before,after);
    }

    // 更新
    @Transactional
    public void updateWithChild(SysAclModule before,SysAclModule after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<SysAclModule> childAclModuleList = sysAclModuleDao.getChildAclModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(childAclModuleList)){
                for (SysAclModule aclModule : childAclModuleList){
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + oldLevelPrefix.substring(oldLevelPrefix.length());
                    }
                }
                // 更新所有子部门 level
                sysAclModuleDao.batchUpdateLevel(childAclModuleList);
            }

        }
        // 更新数据
        sysAclModuleDao.updateByPrimaryKeySelective(after);
    }

    /**
     * 判断同级下部门名称是否存在
     * @param parentId
     * @param id
     * @param name
     * @return
     */
    private Boolean checkExist(Integer parentId,String name,Integer id){
        return sysAclModuleDao.countByNameAndParent(parentId,name,id) > 0;
    }

    /**
     * 获取级别
     * @param aclModuleId
     * @return
     */
    private String getLevel(Integer aclModuleId){
        SysAclModule aclModule = sysAclModuleDao.selectByPrimaryKey(aclModuleId);
        if (aclModule == null){
            return null;
        }
        return aclModule.getLevel();
    }
}

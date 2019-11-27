package cn.xiaochi.service;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysDeptDao;
import cn.xiaochi.exception.ParamException;
import cn.xiaochi.model.SysDept;
import cn.xiaochi.param.DeptParam;
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
public class SysDeptService {

    @Resource
    private SysDeptDao sysDeptDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 添加部门
     * @param deptParam
     */
    public void save(DeptParam deptParam) throws ParamException {
         if (checkExist(deptParam.getParentId(), deptParam.getName(),deptParam.getId())){
             throw new ParamException("部门名称已存在");
         }
         // 使用 lombok的 @build 注释进行设置值
        SysDept dept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();

        dept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());

        sysDeptDao.insertSelective(dept);

        // 记录日志
        sysLogService.saveDeptLog(null,dept);
    }

    /**
     * 更新部门
     * @param deptParam
     */
    public void update(DeptParam deptParam) throws ParamException {
        // 判断更新的数据是否存在
        SysDept deptBefore = sysDeptDao.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(deptBefore,"待更新的部门不存在");

        if (checkExist(deptParam.getParentId(), deptParam.getName(),deptParam.getId())){
            throw new ParamException("部门名称已存在");
        }
        // 使用 lombok的 @build 注释进行设置值
        SysDept deptAfter = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        deptAfter.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        deptAfter.setOperator(RequestHolder.getCurrentUser().getUsername());
        deptAfter.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        deptAfter.setOperateTime(new Date());

        // 更新数据
        updateWithChild(deptBefore,deptAfter);

        // 记录日志
        sysLogService.saveDeptLog(deptBefore,deptAfter);
    }

    // 更新
    @Transactional
    public void updateWithChild(SysDept before,SysDept after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            List<SysDept> childDeptList = sysDeptDao.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(childDeptList)){
                for (SysDept dept : childDeptList){
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + oldLevelPrefix.substring(oldLevelPrefix.length());
                    }
                }
                // 更新所有子部门 level
                sysDeptDao.batchUpdateLevel(childDeptList);
            }

        }
        // 更新数据
        sysDeptDao.updateByPrimaryKeySelective(after);
    }

    /**
     * 判断同级下部门名称是否存在
     * @param parentId
     * @param id
     * @param name
     * @return
     */
    private Boolean checkExist(Integer parentId,String name,Integer id){
        return sysDeptDao.countByNameAndParent(parentId,name,id) > 0;
    }

    /**
     * 获取级别
     * @param depeId
     * @return
     */
    private String getLevel(Integer depeId){
        SysDept dept = sysDeptDao.selectByPrimaryKey(depeId);
        if (dept == null){
            return null;
        }
        return dept.getLevel();
    }
}

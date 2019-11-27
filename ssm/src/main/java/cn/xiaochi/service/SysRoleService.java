package cn.xiaochi.service;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dao.SysRoleDao;
import cn.xiaochi.exception.ParamException;
import cn.xiaochi.model.SysRole;
import cn.xiaochi.param.RoleParm;
import cn.xiaochi.util.IpUtil;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;
    @Resource
    private SysLogService sysLogService;

    /**
     * 获取权限点列表
     * @return
     */
    public List<SysRole> getAllRole(){
        return sysRoleDao.selectAll();
    }

    /**
     * 添加角色
     * @param roleParm
     */
    public void save(RoleParm roleParm) throws ParamException {
        if (checkExist(roleParm.getName(),roleParm.getId())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole role = SysRole.builder().name(roleParm.getName()).type(roleParm.getType()).status(roleParm.getStatus()).remark(roleParm.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());

        sysRoleDao.insertSelective(role);

        // 记录日志
        sysLogService.saveRoleLog(null,role);
    }

    /**
     * 更新角色
     * @param roleParm
     */
    public void update(RoleParm roleParm) throws ParamException {
        if (checkExist(roleParm.getName(),roleParm.getId())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleDao.selectByPrimaryKey(roleParm.getId());
        Preconditions.checkNotNull(before,"待更新的角色不存在");

        SysRole after = SysRole.builder().id(roleParm.getId()).name(roleParm.getName()).type(roleParm.getType()).status(roleParm.getStatus()).remark(roleParm.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysRoleDao.updateByPrimaryKeySelective(after);

        // 记录日志
        sysLogService.saveRoleLog(before,after);
    }

    /**
     * 判断角色名称是否存在
     * @param id
     * @param name
     * @return
     */
    private Boolean checkExist(String name,Integer id){
        return sysRoleDao.countByName(name,id) > 0;
    }
}

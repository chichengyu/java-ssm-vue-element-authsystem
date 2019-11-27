package cn.xiaochi.controller;

import cn.xiaochi.model.SysRole;
import cn.xiaochi.param.RoleParm;
import cn.xiaochi.service.SysRoleService;
import cn.xiaochi.service.SysTreeService;
import cn.xiaochi.util.BeanValidator;
import cn.xiaochi.util.PageUtil;
import cn.xiaochi.util.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 获取角色权限结构树
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public ResponseResult roleTree(@RequestParam("roleId") int roleId){
        return ResponseResult.success(sysTreeService.roleTree(roleId));
    }

    /**
     * 获取权限点列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseResult list(Integer page){
        // 分页
        if (ObjectUtils.isEmpty(page)){
            page = PageUtil.pageNum;
        }
        PageHelper.startPage(page,PageUtil.pageSize);
        List<SysRole> role = sysRoleService.getAllRole();
        PageInfo<SysRole> sysRolePageInfo = new PageInfo<>(role);
        HashMap<String, Object> map = new HashMap<>();
        map.put("currentPage",sysRolePageInfo.getPageNum());
        map.put("total",sysRolePageInfo.getPages());
        map.put("list",sysRolePageInfo.getList());
        return ResponseResult.success(map);
    }

    /**
     * 添加角色
     * @param roleParm
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseResult saveRole(RoleParm roleParm){
        try{
            String result = BeanValidator.validateObject(roleParm);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysRoleService.save(roleParm);
            return ResponseResult.success("添加成功");
        }catch (Exception e){
            return ResponseResult.error("添加失败");
        }
    }

    /**
     * 更新角色
     * @param roleParm
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult updateRole(RoleParm roleParm){
        try{
            String result = BeanValidator.validateObject(roleParm);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysRoleService.update(roleParm);
            return ResponseResult.success("更新成功");
        }catch (Exception e){
            return ResponseResult.error("更新失败");
        }
    }
}

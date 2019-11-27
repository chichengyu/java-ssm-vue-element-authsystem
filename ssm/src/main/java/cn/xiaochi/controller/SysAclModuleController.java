package cn.xiaochi.controller;


import cn.xiaochi.dto.AclModuleLevelDto;
import cn.xiaochi.dto.DeptLevelDto;
import cn.xiaochi.param.AclModuleParam;
import cn.xiaochi.service.SysAclModuleService;
import cn.xiaochi.service.SysTreeService;
import cn.xiaochi.util.BeanValidator;
import cn.xiaochi.util.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysTreeService sysTreeService;

    /**
     * 获取权限模块结构树数据
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public ResponseResult tree(){
        List<AclModuleLevelDto> dtoList = sysTreeService.aclModuleTree();
        return ResponseResult.success(dtoList);
    }

    /**
     * 添加权限模块
     * @param aclModuleParam
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseResult saveAclModule(AclModuleParam aclModuleParam){
        try{
            String result = BeanValidator.validateObject(aclModuleParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysAclModuleService.save(aclModuleParam);
            return ResponseResult.success("添加成功");
        }catch (Exception e){
            return ResponseResult.error("添加失败");
        }
    }

    /**
     * 更新权限模块
     * @param aclModuleParam
     * @return
     */
    public ResponseResult updateAclModule(AclModuleParam aclModuleParam){
        try{
            String result = BeanValidator.validateObject(aclModuleParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysAclModuleService.update(aclModuleParam);
            return ResponseResult.success("更新成功");
        }catch (Exception e){
            return ResponseResult.error("更新失败");
        }
    }
}

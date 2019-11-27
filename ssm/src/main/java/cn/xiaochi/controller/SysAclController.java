package cn.xiaochi.controller;


import cn.xiaochi.param.AclParam;
import cn.xiaochi.service.SysAclSerivice;
import cn.xiaochi.util.BeanValidator;
import cn.xiaochi.util.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sys/acl")
public class SysAclController {

    @Resource
    private SysAclSerivice sysAclSerivice;

    /**
     * 添加权限点
     * @param aclParam
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseResult saveAcl(AclParam aclParam){
        try{
            String result = BeanValidator.validateObject(aclParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysAclSerivice.save(aclParam);
            return ResponseResult.success();
        }catch (Exception e){
            return ResponseResult.error("添加失败");
        }
    }

    /**
     * 更新权限点
     * @param aclParam
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult updateAcl(AclParam aclParam){
        try{
            String result = BeanValidator.validateObject(aclParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysAclSerivice.update(aclParam);
            return ResponseResult.success();
        }catch (Exception e){
            return ResponseResult.error("更新失败");
        }
    }
}

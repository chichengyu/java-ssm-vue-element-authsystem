package cn.xiaochi.controller;

import cn.xiaochi.model.SysUser;
import cn.xiaochi.service.SysUserService;
import cn.xiaochi.util.MD5Util;
import cn.xiaochi.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private SysUserService sysUserService;


    /**
     * 退出登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult logout(HttpServletRequest request){
        try{
            request.getSession().invalidate();
            return ResponseResult.success("退出成功");
        }catch (Exception e){
            logger.warn("logout error:{}",e);
            return ResponseResult.error("退出失败");
        }
    }

    /**
     * 登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult login(@RequestParam String username,@RequestParam String password, HttpServletRequest request){
        try{
            if (StringUtils.isEmpty(username)){
                return ResponseResult.error("账号不能为空");
            }
            if (StringUtils.isEmpty(password)){
                return ResponseResult.error("密码不能为空");
            }
            SysUser user = sysUserService.findByKeyWord(username);
            if (ObjectUtils.isEmpty(user)){
                return ResponseResult.error("找不到指定的用户");
            }else if (!user.getPassword().equals(MD5Util.encryptByMD5(password))){
                return ResponseResult.error("用户名或密码错误");
            }else if (user.getStatus() != 1){
                return ResponseResult.error("用户已被冻结，请联系管理员");
            }else {
                request.getSession(true).setAttribute("user",user);
                request.getSession(true).setAttribute("uid",user.getId());
                Map<String,Object> map = new HashMap<>();
                map.put("name",user.getUsername());
                map.put("roles",1);// 角色超级管理员
                map.put("rules",new int[0]);// 角色超级管理员
                return ResponseResult.success("登录成功",map);
            }
        }catch (Exception e){
            logger.warn("login error:{}",e);
            return ResponseResult.error("登录失败");
        }
    }
}

package cn.xiaochi.controller;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.dto.DeptLevelDto;
import cn.xiaochi.model.SysUser;
import cn.xiaochi.param.UserParam;
import cn.xiaochi.service.SysTreeService;
import cn.xiaochi.service.SysUserService;
import cn.xiaochi.util.BeanValidator;
import cn.xiaochi.util.PageUtil;
import cn.xiaochi.util.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 添加用户
     * @param userParam
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseResult save(UserParam userParam){
        try{
            // 验证参数
            String result = BeanValidator.validateObject(userParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysUserService.save(userParam);
            return ResponseResult.success("添加成功");
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 修改用户
     * @param userParam
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult update(UserParam userParam){
        try{
            // 验证参数
            String result = BeanValidator.validateObject(userParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            sysUserService.update(userParam);
            return ResponseResult.success("修改成功");
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 用户列表
     * @param page 当前页
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseResult list(Integer page){
        try{
            // 分页
            if (page == null){
                page = PageUtil.pageNum;
            }
            PageHelper.startPage(page,PageUtil.pageSize);

            Integer deptId = RequestHolder.getCurrentUser().getDeptId();
            if (ObjectUtils.isEmpty(deptId)){
                deptId = 0;
            }
            List<SysUser> sysUsers = sysUserService.selectAll(deptId);

            PageInfo<SysUser> sysUserPageInfo = new PageInfo<SysUser>(sysUsers);

            HashMap<String, Object> map = new HashMap<>();
            map.put("list",sysUserPageInfo.getList());
            map.put("currentPage",sysUserPageInfo.getPageNum());
            map.put("pages",sysUserPageInfo.getPages());
            map.put("total",sysUserPageInfo.getTotal());
            return ResponseResult.success(map);
        }catch (Exception e){
            return ResponseResult.error();
        }
    }
}

package cn.xiaochi.controller;

import cn.xiaochi.dto.DeptLevelDto;
import cn.xiaochi.param.DeptParam;
import cn.xiaochi.service.SysDeptService;
import cn.xiaochi.service.SysTreeService;
import cn.xiaochi.util.BeanValidator;
import cn.xiaochi.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Resource
    private SysDeptService deptService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/page")
    public ModelAndView page(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("username","aaaaaaaaaa");
        mv.setViewName("dept");
        return mv;
    }

    /**
     * 添加部门
     * @param deptParam
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ResponseResult saveDept(DeptParam deptParam){
        try{
            // 验证参数
            String result = BeanValidator.validateObject(deptParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            deptService.save(deptParam);
            return ResponseResult.success("添加成功");
        }catch (Exception e){
            return ResponseResult.error("添加失败");
        }
    }

    /**
     * 获取部门结构树数据
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public ResponseResult tree(){
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return ResponseResult.success(dtoList);
    }

    /**
     * 更新部门
     * @param deptParam
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseResult updateDept(DeptParam deptParam){
        try{
            // 验证参数
            String result = BeanValidator.validateObject(deptParam);
            if (result.length() > 0){
                return ResponseResult.error(result);
            }
            deptService.update(deptParam);
            return ResponseResult.success("修改成功");
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
    }
}

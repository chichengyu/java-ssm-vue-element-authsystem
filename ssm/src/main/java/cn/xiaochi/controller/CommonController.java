package cn.xiaochi.controller;

import cn.xiaochi.util.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于在拦截器中返回 json 数据
 */
@RestController
public class CommonController {

    @RequestMapping("/common/toJson")
    @ResponseBody
    public ResponseResult error(HttpServletRequest request){
        ResponseResult response = new ResponseResult();
        response.setMsg((String) request.getAttribute("msg"));
        return response;
    }
}

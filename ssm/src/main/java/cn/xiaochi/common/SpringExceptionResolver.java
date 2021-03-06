package cn.xiaochi.common;

import cn.xiaochi.exception.ParamException;
import cn.xiaochi.exception.PermissionException;
import cn.xiaochi.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接管异常，由于我用的时候没有效果，所以抛弃没用
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";


        // 这里我们要求项目中所有请求json数据，都使用.json结尾
//        if (url.endsWith(".json")) {
//            if (ex instanceof PermissionException || ex instanceof ParamException) {
//                ResponseResult result = ResponseResult.error(ex.getMessage());
//                mv = new ModelAndView("jsonView", result.toMap());
//            } else {
//                log.error("unknown json exception, url:" + url, ex);
//                ResponseResult result = ResponseResult.error(defaultMsg);
//                mv = new ModelAndView("jsonView", result.toMap());
//            }
//        } else if (url.endsWith(".page")){ // 这里我们要求项目中所有请求page页面，都使用.page结尾
//            log.error("unknown page exception, url:" + url, ex);
//            ResponseResult result = ResponseResult.error(defaultMsg);
//            mv = new ModelAndView("exception", result.toMap());
//        } else {
//            log.error("unknow exception, url:" + url, ex);
//            ResponseResult result = ResponseResult.error(defaultMsg);
//            mv = new ModelAndView("jsonView", result.toMap());
//        }

        return new ModelAndView();
    }
}

package cn.xiaochi.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**  ======== Http 请求前后全局监听工具 ========
 *
 * http 请求前后的监听工具（全局）
 * 注意：需要在 springmvc.xml 配置文件中 注册该监听类
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

    private final String REQUEST_START_TIME = "request_start_time";

    /**
     * 请求处理前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
        long startTime = System.currentTimeMillis();
        logger.info("request start. url:{}, params:{}", url, parameterMap.toString());
        request.setAttribute(REQUEST_START_TIME,startTime);
        return true;
    }

    /**
     * 请求处理完成后
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // String url = request.getRequestURI().toString();
        // long startTime = (long) request.getAttribute(REQUEST_START_TIME);
        // long endTime = System.currentTimeMillis();
        // logger.info("request finished. url:{}, cost:{}", url, endTime - startTime);
    }

    /**
     * 请求处理完成后，与 postHandle 不同的是，请求后异常也会进入这个方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURI().toString();
        long startTime = (long) request.getAttribute(REQUEST_START_TIME);
        long endTime = System.currentTimeMillis();
        logger.info("request completed. url:{}, cost:{}", url, endTime - startTime);

        // 请求完后，移除当前用户的请求进程与存放用户信息的进程
        removeThreadLocalInfo();
    }

    /**
     * 请求完后，移除当前用户的请求进程与存放用户信息的进程
     */
    public void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}

package cn.xiaochi.filter;

import cn.xiaochi.common.ApplicationContextHelper;
import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.model.SysUser;
import cn.xiaochi.service.SysCoreService;
import cn.xiaochi.util.JsonMapper;
import cn.xiaochi.util.ResponseResult;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ===== 权限拦截 =====
 * 需要在 web.xml 文件中配置 Filter
 */
@Slf4j
public class AclControllerFilter implements Filter {

    // ==== 定义全局需要过滤的 url
    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map parameterMap = request.getParameterMap();

        if (exclusionUrlSet.contains(servletPath)){
            filterChain.doFilter(request,response);
            return;
        }
        // 未登录无权限
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null){
            log.info("someone visit {}, but no login, parameter:{}",servletPath, JsonMapper.objToString(parameterMap));
            noAuth(request,response);
            return;
        }
        // 无权限访问
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)){
            log.info("{} visit {}, but no login, parameter:{}", JsonMapper.objToString(sysUser), servletPath, JsonMapper.objToString(parameterMap));
            noAuth(request, response);
            return;
        }
        filterChain.doFilter(request,response);
        return;
    }
    // 没有权限访问
    private void noAuth(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/json");
        ResponseResult responseResult = new ResponseResult();
        responseResult.setMsg("没有访问权限，请联系管理员");
        response.getWriter().print(JsonMapper.objToString(responseResult));
        return;
    }

    @Override
    public void destroy() {

    }
}

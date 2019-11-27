package cn.xiaochi.filter;

import cn.xiaochi.common.RequestHolder;
import cn.xiaochi.model.SysUser;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** ========= 拦截器 中添加 用户信息进程与请求进程 ==========
 *
 * 需要在 web.xml 文件中配置 Filter
 */
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // 添加用户进程 与 用户请求进程，后面直接从进程去信息
        SysUser user = (SysUser) req.getSession().getAttribute("user");
        if (user == null){ // 说明用户未登录
            req.setAttribute("msg","请先登录");
            // 返回 json 格式数据
            req.getRequestDispatcher("/common/toJson").forward(req,resp);
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(req);

        filterChain.doFilter(servletRequest,servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}

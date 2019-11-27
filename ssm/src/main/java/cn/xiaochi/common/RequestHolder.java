package cn.xiaochi.common;

import cn.xiaochi.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/** ======= 高并发时很有效 =======
 *
 * 把每个用户的信息与请求存在不同的进程中
 */
public class RequestHolder {
    // 用户进程，把每个用户的信息存在不同的进程中
    public static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();
    // 用户进程，把每个用户的请求存在不同的进程中
    public static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(SysUser sysUser){
        userHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static SysUser getCurrentUser(){
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest(){
        return requestHolder.get();
    }

    // 进程关闭时移除
    public static void remove(){
        userHolder.remove();
        requestHolder.remove();
    }
}

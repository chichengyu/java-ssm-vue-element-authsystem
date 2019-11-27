package cn.xiaochi.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**  ======== 获取 spring 的上下文工具 ==========
 *
 * 全局工具获取 Spring 容器中已初始化的 Bean， 获取 spring 的上下文
 * 注意：需要在 springmvc.xml 配置文件中注入启动时加载，不要懒加载
 * */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    // 启动 spring 的时候，会执行该方法
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取 Spring 容器中已初始化的 Bean
     * @param clazz
     * @param <T>
     * @return
     */
    // 这里public后面<T>这里的T限定了后面的返回类型必须是T，参数也必须是T
    // Class<T> 是约束 clazz 必须是引用类型，不能是值类型
    public static <T>T popBean(Class<T> clazz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static <T>T popBean(String name,Class<T> clazz){
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name,clazz);
    }
}

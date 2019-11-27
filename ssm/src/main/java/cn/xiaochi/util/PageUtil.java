package cn.xiaochi.util;

import lombok.Getter;
import lombok.Setter;

public class PageUtil {
    @Getter
    @Setter
    public static int pageNum = 1;// 默认第一页

    @Getter
    @Setter
    public static int pageSize = 5;// 每页显示5条
}

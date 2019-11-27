package cn.xiaochi.util;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**    ======== JSON 转换工具 ========
 *
 * jackson 工具：将一个json字符串转化为一个类对象
 * */
// 记录日志
@Slf4j
public class JsonMapper {
    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    // 初始化设置
    {
        // config比如：序列化的时候不存在的字段转化后就不存在该字段
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    /**
     * 对象转字符串
     * @param src
     * @param <T>
     * @return
     */
    public static <T>String objToString(T src){
        if (src == null){
            return null;
        }
        try{
            return src instanceof String ? (String) src :objectMapper.writeValueAsString(src);
        }catch (Exception e){
            logger.warn("parse object to String exception, error:{}", e);
            return null;
        }
    }

    /**
     * 字符串转对象
     * @param src
     * @param typeReference
     * @param <T>
     * @return
     */
    // 这里public后面<T>这里的T限定了后面的返回类型必须是T，参数也必须是T
    public static <T>T stringToObj(String src,TypeReference<T> typeReference){
        if (src == null || typeReference == null){
            return null;
        }
        try{
            return (T)(typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src,typeReference));
        }catch (Exception e){
            logger.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, typeReference.getType(), e);
            return null;
        }
    }
}

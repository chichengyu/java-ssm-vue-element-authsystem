package cn.xiaochi.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashMap;


/** ======== 全局参数效验工具 ========
 *
 * 全局参数效验工具
 * */
public class BeanValidator {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Map<String,String> validate(T t, Class... groups){
        Validator validator = validatorFactory.getValidator();
        Set validateReslt = validator.validate(t, groups);
        Map data = Maps.newLinkedHashMap();
        if (validateReslt.isEmpty()){
            data = Collections.emptyMap();
        }else{
            // 与 new LinkedHashMap 一样的效果
            //LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validateReslt.iterator();
            while (iterator.hasNext()){
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                // errors.put(violation.getPropertyPath().toString(),violation.getMessage());
                data.put(violation.getPropertyPath().toString(),violation.getMessage());
            }
            //return errors;
        }
        // throw new Exception(data.toString());
        return data;
    }

    public static Map<String,String> validateList(Collection<?> collection){
        Preconditions.checkNotNull(collection);
        Iterator<?> iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()){
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object,new Class[0]);
        }while (errors.isEmpty());
        return errors;
    }

    /**
     * 外部调用效验方法
     * */
    public static String validateObject(Object first,Object... objects) {
        Map data = null;
        if (objects != null && objects.length > 0){
            // return validateList(Lists.asList(first,objects));
            data = validateList(Lists.asList(first,objects));
        }else{
            // return validate(first,new Class[0]);
            data = validate(first,new Class[0]);
        }
        if (data.isEmpty()){ // 为空表示验证通过
            // return (Boolean) true;
             return "";
        }
        // 验证不通过
        // throw new Exception(data.toString());
        return getMsg(data.toString());
    }

    /**
     * map转str
     * @param map
     * @return
     */
    public static String getMapToString(Map<String,String> map){
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if (map.get(keyArray[i]).trim().length() > 0) {
                sb.append(keyArray[i]).append("=").append(map.get(keyArray[i]).trim());
            }
            if(i != keyArray.length-1){
                // sb.append("&");
                sb.append(";");
            }
        }
        return sb.toString();
    }

    /**
     * String转map
     * @param str
     * @return
     */
    public static Map<String,String> getStringToMap(String str){
        //判断str是否有值
        if(null == str || "".equals(str)){
            return null;
        }
        //根据&截取
        String[] strings = str.substring(1,str.length()-1).split(",");
        //设置HashMap长度
        //int mapLength = strings.length;
        //判断hashMap的长度是否是2的幂。
        //if((strings.length % 2) != 0){
         //   mapLength = mapLength+1;
        //}

        Map<String,String> map = new HashMap<>(strings.length);
        //循环加入map集合
        for (int i = 0; i < strings.length; i++) {
            //截取一组字符串
            String[] strArray = strings[i].trim().split("=");
            //strArray[0]为KEY  strArray[1]为值
            map.put(strArray[0],strArray[1]);
        }
        return map;
    }

    /**
     * 返回效验失败的具体提示信息
     * @return
     */
    public static String getMsg(String str){
        int length = str.length();
        return str.substring(1,length-1).split(",")[0].split("=")[1];
    }
}

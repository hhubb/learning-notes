package hbb.istudy.com.Spring笔记.手写Spring.V4.mvc;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestParam;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author binbin
 * @Date 2024 12 25 18 40
 **/
public class HHandlerAdapter {
    public HModelAndView handler(HttpServletRequest req, HHandlerMapping hHandler, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException {
        //1. 先把参数名字（key）和形参的位置（value）建立映射关系，并缓存下来
        Map<String, Integer> paramMap = new HashMap<>();
        Annotation[][] annotations = hHandler.getMethod().getParameterAnnotations();
        for (int i = 0; i <= annotations.length - 1; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof HRequestParam) {
                    String paramName = ((HRequestParam) annotation).value();
                    if (StringUtils.isNotBlank(paramName.trim())) {
                        paramMap.put(paramName, i);
                    }
                }

            }
        }
        Class<?>[] paramTypes = hHandler.getMethod().getParameterTypes();
        for (int i = 0; i <= paramTypes.length - 1; i++) {
            Class<?> type = paramTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramMap.put(type.getName(), i);
            }
        }
        //2. 根据参数位置匹配参数名字，从URL中获取到参数的值
        Object[] paramValues = new Object[paramTypes.length];
        Map<String, String[]> params = req.getParameterMap();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[\\]", "").replaceAll("\\s", "");
            if (!paramMap.containsKey(param.getKey())) {
                continue;
            }
            int index = paramMap.get(param.getKey());
            //涉及到类型强转
            paramValues[index] = caseStringValue(value, paramTypes[index]);

        }
        if (paramMap.containsKey(HttpServletRequest.class.getName())) {
            int index = paramMap.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }
        if (paramMap.containsKey(HttpServletResponse.class.getName())) {
            int index = paramMap.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }
        //3. 组成动态参数列表，传给反射调用方法
        Object result = hHandler.getMethod().invoke(hHandler.getController(), paramValues);
        if (result == null || result instanceof Void) {
            return null;
        }
        boolean isModelAndView = hHandler.getMethod().getReturnType() == HModelAndView.class;
        if (isModelAndView) {
            return (HModelAndView) result;
        }

        return null;
    }

    private Object caseStringValue(String value, Class<?> paramType) {
        if (String.class == paramType) {
            return value;
        }
        if (Integer.class == paramType) {
            return Integer.valueOf(value);
        }
        if (Double.class == paramType) {
            return Double.valueOf(value);
        }
        return value;
    }
}

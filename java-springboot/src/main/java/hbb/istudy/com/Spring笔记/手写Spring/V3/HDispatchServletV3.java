package hbb.istudy.com.Spring笔记.手写Spring.V3;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HController;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestMapping;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestParam;
import hbb.istudy.com.Spring笔记.手写Spring.V3.context.HApplicationContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author binbin
 * @Date 2024 12 17 11 01
 **/
public class HDispatchServletV3 extends HttpServlet {


    //URL和Method的对应关系
    private Map<String, Method> HandlerMapping = new HashMap<>();

    private HApplicationContext applicationContext = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //6. 根据URL委派具体方法调用
        try {
            doDispatcher(req, resp);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        if (!this.HandlerMapping.containsKey(url)) {
            resp.getWriter().write("404");
            return;
        }

        Method method = this.HandlerMapping.get(url);
        //1. 先把参数名字（key）和形参的位置（value）建立映射关系，并缓存下来
        Map<String, Integer> paramMap = new HashMap<>();
        Annotation[][] annotations = method.getParameterAnnotations();
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
        Class<?>[] paramTypes = method.getParameterTypes();
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
            paramValues[index] = value;

        }
        if (paramMap.containsKey(HttpServletRequest.class.getName())) {
            int index = paramMap.get(HttpServletRequest.class.getName());
            paramValues[index] = req;
        }
        if (paramMap.containsKey(HttpServletResponse.class.getName())) {
            int index = paramMap.get(HttpServletResponse.class.getName());
            paramValues[index] = resp;
        }

        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        //3. 组成动态参数列表，传给反射调用方法
        method.invoke(applicationContext.getBean(beanName), paramValues);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
//        //1. 加载配置文件
//        doLoadConfig(config.getInitParameter("contextConfigLocation"));
//        //2. 扫描相关的类
//        doScanner(properties.getProperty("scanPackage"));
//        //3. 初始化IoC容器，将扫描到的类实例化，缓存到IoC容器中
//        doInstance();
//        //4. DI
//        doAutowired();
        //原来的1、2、3、4 放入applicationContext
        applicationContext = new HApplicationContext(config.getInitParameter("contextConfigLocation"));

        //5. 初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("HBB Spring Framework done!");
    }

    private void doInitHandlerMapping() {
        if (applicationContext.getBeanDefinitionCount() == 0) {
            return;
        }
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object instance = applicationContext.getBean(beanName);

            Class<?> clazz = instance.getClass();
            if (!clazz.isAnnotationPresent(HController.class)) {
                continue;
            }
            //获取类上的url
            HRequestMapping classRequestMapping = clazz.getAnnotation(HRequestMapping.class);
            String baseUrl = classRequestMapping.value();
            for (Method method : clazz.getMethods()) {
                if (!clazz.isAnnotationPresent(HRequestMapping.class)) {
                    continue;
                }
                //获取方法上的的url
                HRequestMapping methodRequestMapping = method.getAnnotation(HRequestMapping.class);
                //拼接URL
                String url = ("/" + baseUrl + "/" + methodRequestMapping.value()).replaceAll("/+", "/");
                HandlerMapping.put(url, method);
                System.out.println("method:" + url + "——>" + method);
            }
        }

    }


    //首字母小写
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


}

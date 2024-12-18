package hbb.istudy.com.Spring笔记.手写Spring.V1;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HAutowired;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HController;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestMapping;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestParam;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author binbin
 * @Date 2024 12 17 11 01
 **/
public class HDispatchServlet extends HttpServlet {
    //配置文件
    private Properties properties = new Properties();
    //缓存满足扫描规则的类的全路径
    private List<String> classNames = new ArrayList<>();
    //IoC容器
    private Map<String, Object> IoCMap = new HashMap<>();

    //URL和Method的对应关系
    private Map<String, Method> HandlerMapping = new HashMap<>();

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
        ApplicationContext

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
        for (Map.Entry<String, String[]> param:params.entrySet()){
            String value= Arrays.toString(param.getValue()).replaceAll("\\[\\]","").replaceAll("\\s","");
            if (!paramMap.containsKey(param.getKey())){
                continue;
            }
            int index=paramMap.get(param.getKey());
            //涉及到类型强转
            paramValues[index]=value;

        }
        if (paramMap.containsKey(HttpServletRequest.class.getName())){
            int index=paramMap.get(HttpServletRequest.class.getName());
            paramValues[index]=req;
        }
        if (paramMap.containsKey(HttpServletResponse.class.getName())){
            int index=paramMap.get(HttpServletResponse.class.getName());
            paramValues[index]=resp;
        }

        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        //3. 组成动态参数列表，传给反射调用方法
        method.invoke(IoCMap.get(beanName), paramValues);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1. 加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //2. 扫描相关的类
        doScanner(properties.getProperty("scanPackage"));
        //3. 初始化IoC容器，将扫描到的类实例化，缓存到IoC容器中
        doInstance();
        //4. DI
        doAutowired();

        //5. 初始化HandlerMapping
        doInitHandlerMapping();

        System.out.println("HBB Spring Framework done!");
    }

    private void doInitHandlerMapping() {
        if (IoCMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : IoCMap.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
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

    /**
     * 获取到扫描得到的bean后，看是否有注入的注解，如果有则进行注入
     */
    private void doAutowired() {
        if (IoCMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : IoCMap.entrySet()) {
            //getDeclaredFields 忽略字段修饰符（private/public/ protected/default都会被拿到）
            for (Field field : entry.getValue().getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(HAutowired.class)) {
                    continue;
                }
                HAutowired autowired = field.getAnnotation(HAutowired.class);
                String beanName = autowired.value();
                if (StringUtils.isBlank(beanName)) {
                    beanName = field.getType().getName();
                }
                //强制访问加了private的字段
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), IoCMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 1. 名称重复用别名
     * 2. 注解在接口需要扫描加载他的实现类
     */
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                String alias = "";
                if (clazz.isAnnotationPresent(HController.class)) {
                    //如果多个包下出现相同类名，优先使用别名
                    HController controller = clazz.getAnnotation(HController.class);
                    alias = controller.value();
                } else if (clazz.isAnnotationPresent(HService.class)) {
                    HService service = clazz.getAnnotation(HService.class);
                    alias = service.value();

                } else {
                    continue;
                }
                //如果没设置别名默认类名首字母小写
                String beanName = StringUtils.isBlank(alias) ? toLowerFirstCase(clazz.getSimpleName()) : alias;
                Object instance = clazz.newInstance();
                IoCMap.put(beanName, instance);
                //如果注解在接口上，只能初始化实现类
                for (Class<?> i : clazz.getInterfaces()) {
                    if (IoCMap.containsKey(i.getName())) {
                        throw new Exception("Bean 【" + i.getName() + "】 名称重复！请使用别名！");
                    }
                    IoCMap.put(i.getName(), instance);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //首字母小写
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 扫描到符合包路径规则的所有class文件
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        //包路径替换成文件夹路径
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                //全类名
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }

        }
    }

    /**
     * 根据配置文件路径获取配置文件
     *
     * @param configPath
     */
    private void doLoadConfig(String configPath) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(configPath);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

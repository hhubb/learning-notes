package hbb.istudy.com.Spring笔记.手写Spring.V4;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HController;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestMapping;
import hbb.istudy.com.Spring笔记.手写Spring.V3.context.HApplicationContext;
import hbb.istudy.com.Spring笔记.手写Spring.V4.mvc.HHandlerAdapter;
import hbb.istudy.com.Spring笔记.手写Spring.V4.mvc.HHandlerMapping;
import hbb.istudy.com.Spring笔记.手写Spring.V4.mvc.HModelAndView;
import hbb.istudy.com.Spring笔记.手写Spring.V4.mvc.HView;
import hbb.istudy.com.Spring笔记.手写Spring.V4.mvc.HViewResolvers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author binbin
 * @Date 2024 12 17 11 01
 **/
public class HDispatchServletV4 extends HttpServlet {


    //URL和Method的对应关系
    private List<HHandlerMapping> HandlerMapping = new ArrayList<>();

    private Map<HHandlerMapping, HHandlerAdapter> handlerAdapterMap = new HashMap<>();

    private HApplicationContext applicationContext = null;
    private List<HViewResolvers> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //6. 根据URL委派具体方法调用
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            HModelAndView mv = new HModelAndView("500");
            Map<String, Object> result = mv.getModel();
            result.put("detail", "500");
            result.put("error", e);
            processDispatchResult(req, resp, new HModelAndView("500"));
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1. 根据URL拿到对应的Handler
        HHandlerMapping hHandler = getHandler(req);
        if (null == hHandler) {
            processDispatchResult(req, resp, new HModelAndView("404"));
            return;
        }
        //2. 根据HandlerMapping拿到HandlerAdapter
        HHandlerAdapter adapter = getHandlerAdapter(hHandler);
        //3. 根据HandlerAdapter拿到对应的ModelAndView
        HModelAndView mv = adapter.handler(req, hHandler,resp);
        //4. 根据viewResolver找到对应的View对象
        processDispatchResult(req, resp, mv);
        //3. 通过View对象渲染页面并返回

    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, HModelAndView mv) throws IOException {
        if (null==mv){
            return;
        }
        if (this.viewResolvers.isEmpty()){
            return;
        }
        for (HViewResolvers resolver:viewResolvers){
           HView view= resolver.resolveViewName(mv.getViewName());
            view.render(mv.getModel(),req,resp);
        }
    }

    private HHandlerAdapter getHandlerAdapter(HHandlerMapping hHandler) {
        if (this.handlerAdapterMap.isEmpty()) {
            return null;
        }
        return handlerAdapterMap.get(hHandler);
    }

    private HHandlerMapping getHandler(HttpServletRequest req) {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (HHandlerMapping hHandlerMapping : this.HandlerMapping) {
            Matcher matcher = hHandlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return hHandlerMapping;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //原来的1、2、3、4 放入applicationContext
        applicationContext = new HApplicationContext(config.getInitParameter("contextConfigLocation"));

        //初始化MVC 9大组件
        initStrategies(applicationContext);

        System.out.println("HBB Spring Framework done!");
    }

    private void initStrategies(HApplicationContext context) {
        //initMultipartResolver(context);
        //  initLocaleResolver(context);
        //  initThemeResolver(context);
        //初始化HandlerMappings
        initHandlerMappings(context);
        //初始化参数适配器
        initHandlerAdapters(context);
        //   initHandlerExceptionResolvers(context);
        //  initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        // initFlashMapManager(context);
    }

    private void initHandlerMappings(HApplicationContext context) {
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
                String regex = ("/" + baseUrl + "/" + methodRequestMapping.value()).replaceAll("\\*", ".*").replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                HandlerMapping.add(new HHandlerMapping(pattern, method, instance));
                System.out.println("method:" + regex + "——>" + method);
            }
        }
    }


    private void initHandlerAdapters(HApplicationContext context) {
        //一个HandlerMapping 对应一个HandlerAdapter
        for (HHandlerMapping hHandlerMapping : HandlerMapping) {
            handlerAdapterMap.put(hHandlerMapping, new HHandlerAdapter());
        }
    }

    private void initViewResolvers(HApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new HViewResolvers(templateRoot));
        }
    }


}

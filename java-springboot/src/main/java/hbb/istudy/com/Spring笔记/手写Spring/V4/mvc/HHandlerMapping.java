package hbb.istudy.com.Spring笔记.手写Spring.V4.mvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @Author binbin
 * @Date 2024 12 25 18 22
 **/
public class HHandlerMapping {
    private Pattern pattern;
    private Method method;
    private Object controller;
    public HHandlerMapping(Pattern pattern, Method method,Object controller) {
        this.pattern=pattern;
        this.method=method;
        this.controller=controller;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return controller;
    }
}

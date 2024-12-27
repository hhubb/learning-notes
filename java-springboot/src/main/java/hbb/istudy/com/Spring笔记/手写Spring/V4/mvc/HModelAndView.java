package hbb.istudy.com.Spring笔记.手写Spring.V4.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author binbin
 * @Date 2024 12 26 16 17
 **/
public class HModelAndView {
    private String viewName;
    private Map<String, Object> model=new HashMap<>();

    public HModelAndView(String s) {
        this.viewName = viewName;
    }

    public HModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}

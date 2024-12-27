package hbb.istudy.com.Spring笔记.手写Spring.V3.beans;

/**
 * Bean 装饰器
 *
 * @Author binbin
 * @Date 2024 12 19 13 14
 **/
public class HBeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public HBeanWrapper(Object instance){

    }
    public Object getWrapperInstance() {
        return this.wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return this.wrapperClass;
    }
}

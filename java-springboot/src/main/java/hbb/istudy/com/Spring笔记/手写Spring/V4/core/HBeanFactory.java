package hbb.istudy.com.Spring笔记.手写Spring.V4.core;

/**
 * 创建对象工厂最顶层的接口
 * @Author binbin
 * @Date 2024 12 19 09 46
 **/
public interface HBeanFactory {
    Object getBean(Class beanClass);

    Object getBean(String beanName);
}

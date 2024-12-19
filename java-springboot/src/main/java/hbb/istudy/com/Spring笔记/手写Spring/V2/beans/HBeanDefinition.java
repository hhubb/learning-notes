package hbb.istudy.com.Spring笔记.手写Spring.V2.beans;

/**
 * @Author binbin
 * @Date 2024 12 19 09 57
 **/
public class HBeanDefinition {

    /**
     * 是否延迟加载
     *
     * @return
     */
    public boolean isLazyInit() {
        return false;
    }
    //beanName
    private String factoryBeanName;
    //原生类的全类名
    private String beanClassName;

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }
}

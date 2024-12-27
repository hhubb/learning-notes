package hbb.istudy.com.Spring笔记.手写Spring.V4.beans.support;

import hbb.istudy.com.Spring笔记.手写Spring.V4.beans.HBeanDefinition;
import hbb.istudy.com.Spring笔记.手写Spring.V4.core.HBeanFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author binbin
 * @Date 2024 12 19 09 48
 **/
public class HDefaultListableBeanFactory implements HBeanFactory {
    //保存所有bean的配置信息
    public Map<String, HBeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public Object getBean(Class beanClass) {
        return null;
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    public void doRegistryBeanDefinition(List<HBeanDefinition> beanDefinitions) throws Exception {
        for (HBeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception(beanDefinition.getFactoryBeanName() + "bean已存在！");
            }
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }
}

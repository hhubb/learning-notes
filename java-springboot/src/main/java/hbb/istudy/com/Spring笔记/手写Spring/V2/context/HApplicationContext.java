package hbb.istudy.com.Spring笔记.手写Spring.V2.context;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HAutowired;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HController;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HService;
import hbb.istudy.com.Spring笔记.手写Spring.V2.beans.HBeanDefinition;
import hbb.istudy.com.Spring笔记.手写Spring.V2.beans.HBeanWrapper;
import hbb.istudy.com.Spring笔记.手写Spring.V2.beans.support.HBeanDefinitionReader;
import hbb.istudy.com.Spring笔记.手写Spring.V2.beans.support.HDefaultListableBeanFactory;
import hbb.istudy.com.Spring笔记.手写Spring.V2.core.HBeanFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author binbin
 * @Date 2024 12 19 09 49
 **/
public class HApplicationContext implements HBeanFactory {
    //注册器
    private HDefaultListableBeanFactory registry = new HDefaultListableBeanFactory();
    private HBeanDefinitionReader reader = null;

    //三级缓存（终极缓存）
    private Map<String, HBeanWrapper> factoryBeanInstanceCache = new HashMap<>();

    //缓存原生bean对象
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    public HApplicationContext(String... config) {
        try {
            //1. 加载配置文件
            reader = new HBeanDefinitionReader(config);

            //2. 解析配置文件讲所有的配置信息风封装成BeanDefinition
            List<HBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

            //3. 缓存所有的配置信息
            this.registry.doRegistryBeanDefinition(beanDefinitions);

            //4. 创建加载非延迟加载的所有的Bean
            doLoadInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void doLoadInstance() {
        //循环调用getBean()方法
        for (Map.Entry<String, HBeanDefinition> entry : registry.beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            //加载非延迟加载的Bean
            if (!entry.getValue().isLazyInit()) {
                HBeanDefinition bean = (HBeanDefinition) getBean(beanName);

            }

        }
    }


    @Override
    public Object getBean(Class beanClass) {
        return getBean(beanClass.getName());
    }

    /**
     * 从IoC容器中获取Bean对象
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        //1. 先拿到BeanDefinition
        HBeanDefinition beanDefinition = registry.beanDefinitionMap.get(beanName);
        //2. 反射实例化
        Object instance = instanceBean(beanName, beanDefinition);
        //3. 将返回的bean对象封装为BeanWrapper
        HBeanWrapper beanWrapper = new HBeanWrapper(instance);

        //4. 依赖注入
        populateBean(beanName, beanWrapper, beanDefinition);

        //5. 保存在IoC容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);
        return beanWrapper.getWrapperInstance();
    }

    private void populateBean(String beanName, HBeanWrapper beanWrapper, HBeanDefinition beanDefinition) {
        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrapperClass();

        if (!clazz.isAnnotationPresent(HController.class) && !clazz.isAnnotationPresent(HService.class)) {
            return;
        }
        //getDeclaredFields 忽略字段修饰符（private/public/ protected/default都会被拿到）
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(HAutowired.class)) {
                continue;
            }
            HAutowired autowired = field.getAnnotation(HAutowired.class);
            String autoWiredBeanName = autowired.value();
            if (StringUtils.isBlank(autoWiredBeanName)) {
                autoWiredBeanName = field.getType().getName();
            }
            //强制访问加了private的字段
            field.setAccessible(true);
            try {
                if (!this.factoryBeanInstanceCache.containsKey(autoWiredBeanName)) {
                    continue;
                }
                field.set(instance, this.factoryBeanInstanceCache.get(autoWiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 反射实例化
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object instanceBean(String beanName, HBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();
            this.factoryBeanObjectCache.put(beanName, instance);
            //如果是代理对象，触发AOP逻辑 todo


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int getBeanDefinitionCount() {
        return this.registry.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.registry.beanDefinitionMap.keySet().toArray(new String[0]);
    }
}

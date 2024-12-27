package hbb.istudy.com.Spring笔记.手写Spring.V4.context;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HAutowired;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HController;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HService;
import hbb.istudy.com.Spring笔记.手写Spring.V4.beans.HBeanDefinition;
import hbb.istudy.com.Spring笔记.手写Spring.V4.beans.HBeanWrapper;
import hbb.istudy.com.Spring笔记.手写Spring.V4.beans.support.HBeanDefinitionReader;
import hbb.istudy.com.Spring笔记.手写Spring.V4.beans.support.HDefaultListableBeanFactory;
import hbb.istudy.com.Spring笔记.手写Spring.V4.core.HBeanFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author binbin
 * @Date 2024 12 19 09 49
 **/
public class HApplicationContext implements HBeanFactory {
    //注册器
    private HDefaultListableBeanFactory registry = new HDefaultListableBeanFactory();
    private HBeanDefinitionReader reader = null;

    //缓存原生bean对象
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    //classname与bean的映射关系，解决单例问题
    private Map<String, Object> classObjectCache = new HashMap<>();

    //一级缓存成熟bean
    private Map<String, Object> singletonObjects = new HashMap<>();
    //二级缓存早期bean
    private Map<String, Object> earlySingletonObjects = new HashMap<>();
    //三级缓存为直线AOP做准备（终极缓存）
    private Map<String, HBeanWrapper> factoryBeanInstanceCache = new HashMap<>();
    //循环依赖标记
    private Set<String> singletonCurrentlyCreation = new HashSet<>();

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

        //解决循环依赖的入口
        Object singleton = getSingleton(beanName, beanDefinition);
        //如果bean已经创建完成则直接返回
        if (singleton != null) {
            return singleton;
        }
        //标记该bean正在创建
        if (!singletonCurrentlyCreation.contains(beanName)) {
            singletonCurrentlyCreation.add(beanName);
        }

        //2. 反射实例化
        Object instance = instanceBean(beanName, beanDefinition);
        //放入一级缓存
        this.singletonObjects.put(beanName, instance);
        //3. 将返回的bean对象封装为BeanWrapper
        HBeanWrapper beanWrapper = new HBeanWrapper(instance);

        //4. 依赖注入
        populateBean(beanName, beanWrapper, beanDefinition);

        //5. 保存在IoC容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);
        return beanWrapper.getWrapperInstance();
    }

    private Object getSingleton(String beanName, HBeanDefinition beanDefinition) {
        //先到一级缓存中获取
        Object bean = this.singletonObjects.get(beanName);
        //如果一级缓存中没有，但有创建标识，说明是循环依赖
        if (bean == null && singletonCurrentlyCreation.contains(beanName)) {
            bean = earlySingletonObjects.get(beanName);
            //如果二级缓存中没有，则从三级缓存中获取
            if (null == bean) {
                bean = instanceBean(beanName, beanDefinition);
                //将创建出的对象重新放入到二级缓存中
                earlySingletonObjects.put(beanName, bean);
            }
        }


        return bean;
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
                //通过getBean方式获取到依赖的bean
                field.set(instance, getBean(autoWiredBeanName));
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
            //解决单例问题
            if (classObjectCache.containsKey(className)) {
                instance = classObjectCache.get(className);
            }
            this.factoryBeanObjectCache.put(beanName, instance);
            //如果是代理对象，触发AOP逻辑 todo


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return instance;
    }

    public int getBeanDefinitionCount() {
        return this.registry.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.registry.beanDefinitionMap.keySet().toArray(new String[0]);
    }
}

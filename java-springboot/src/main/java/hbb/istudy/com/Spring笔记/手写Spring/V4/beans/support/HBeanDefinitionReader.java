package hbb.istudy.com.Spring笔记.手写Spring.V4.beans.support;

import hbb.istudy.com.Spring笔记.手写Spring.V4.beans.HBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author binbin
 * @Date 2024 12 19 09 55
 **/
public class HBeanDefinitionReader {
    //配置文件
    private Properties properties = new Properties();

    //需要被注册的BeanClassName
    private List<String> registryBeanClasses = new ArrayList<>();

    public HBeanDefinitionReader(String... location) {
        //加载properties文件
        doLoadConfig(location[0]);
        //扫描相关的类
        doScanner(properties.getProperty("scanPackage"));
    }

    public List<HBeanDefinition> loadBeanDefinitions() {
        List<HBeanDefinition> beanDefinitions=new ArrayList<>();
        for (String className:registryBeanClasses){
            try {
                Class<?> clazz=Class.forName(className);
                //如果clazz本身是接口不做处理
                if(clazz.isInterface()){
                    continue;
                }
                //1. 默认类名首字母小写的情况
                beanDefinitions.add(doCreateBeanDefinition(toLowerFirstCase(clazz.getSimpleName()),clazz.getName()));
                //2. 如果类实现了是接口，就用接口的名字作为key，这个class作为value，因为接口没有实例
                for (Class<?> i:clazz.getInterfaces()){
                    beanDefinitions.add(doCreateBeanDefinition(i.getName(),clazz.getName()));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private HBeanDefinition doCreateBeanDefinition(String factoryBeanName, String factoryClassName) {
        HBeanDefinition beanDefinition=new HBeanDefinition();
        beanDefinition.setBeanClassName(factoryClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    //首字母小写
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
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
                registryBeanClasses.add(className);
            }

        }
    }
}

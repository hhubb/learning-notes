package hbb.istudy.com.Spring笔记.手写Spring.V1.test.impl;

import hbb.istudy.com.Spring笔记.手写Spring.V1.test.TestService;

/**
 * @Author binbin
 * @Date 2024 12 17 17 06
 **/
public class TestServiceImpl implements TestService {
    @Override
    public String getName(String name) {
        return "我得名字是 " + name;
    }
}

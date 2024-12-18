package hbb.istudy.com.Spring笔记.手写Spring.V1.test;

import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HAutowired;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HController;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestMapping;
import hbb.istudy.com.Spring笔记.手写Spring.V1.annotation.HRequestParam;

/**
 * @Author binbin
 * @Date 2024 12 17 17 04
 **/
@HController
@HRequestMapping("/test")
public class TestController {
    @HAutowired
    TestService testService;

    @HRequestMapping("/getName")
    public String getName(@HRequestParam("name") String name) {
        return testService.getName(name);
    }
}

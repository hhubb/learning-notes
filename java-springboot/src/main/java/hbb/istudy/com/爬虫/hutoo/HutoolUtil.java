package hbb.istudy.com.爬虫.hutoo;

import cn.hutool.db.Db;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author binbin
 * @Date 2024 06 05 11 07
 **/
public class HutoolUtil {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private static Db db;

    private static DruidDataSource createDataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("");
        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }

    public static Db getDb() {
        if (db == null) {
            db = Db.use(createDataSource());
        }
        return db;
    }
}

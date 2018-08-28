package com.duobi.manager;

import com.duobi.manager.sys.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan(value = "com.duobi.manager") //启用@WebServlet @WebFilter 注解
//@MapperScan(basePackages = "com.duobi.manager")
@EnableTransactionManagement // 启用注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
public class ManagerApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(ManagerApplication.class, args);
        SpringUtil.setApplicationContext(app);
    }

}



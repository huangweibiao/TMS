package com.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TMS运输管理系统启动类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@SpringBootApplication
public class TmsApplication {

    /**
     * 应用程序入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TmsApplication.class, args);
    }
}

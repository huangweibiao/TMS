package com.tms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA配置类
 * 启用JPA审计功能，自动填充创建时间和更新时间
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}

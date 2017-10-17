package org.midnightbsd.appstore.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Arrays;

/**
 * @author Lucas Holt
 */
@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = {"org.midnightbsd.appstore.repository.search"})
public class SearchConfig {
    @Autowired
    private Environment environment;

    @Value("search.package-entry-index")
    private String packageItemIndex;

    @Bean
    public String packageItemIndex() {
        return String.format("%s-%s",packageItemIndex,getEnv());
    }

    public String getEnv() {
        if(environment != null) {
            return Arrays.toString(environment.getActiveProfiles());
        }
        return "";
    }
}
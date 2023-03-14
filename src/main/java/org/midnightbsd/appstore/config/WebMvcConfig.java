package org.midnightbsd.appstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author Lucas Holt
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    public WebMvcConfig() {
        super();
    }

    /**
     * Override default handlers.
     * see http://stackoverflow.com/questions/24837715/spring-boot-with-angularjs-html5mode
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        Integer cachePeriod = 60;

        registry.addResourceHandler("/robots.txt")
                     .addResourceLocations("classpath:/static/robots.txt")
                     .setCachePeriod(cachePeriod);

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(cachePeriod);

        registry.addResourceHandler("/static/styles/**", "/styles/*.css", "/styles/*.map")
                        .addResourceLocations("classpath:/static/styles/")
                        .setCachePeriod(cachePeriod);

        // enable webjars
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
    }


      /** {@inheritDoc} */
      @Override
      public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
          super.configureMessageConverters(converters);
          converters.add(new StringHttpMessageConverter());
          converters.add(new ByteArrayHttpMessageConverter());
      }

}
package org.midnightbsd.appstore;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Lucas Holt
 */
@EnableEurekaClient
@Configuration
@EnableScheduling
@EnableAsync
@EnableJpaRepositories
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {
    @Value("${tomcat.ajp.port}")
       int ajpPort;

       @Value("${tomcat.ajp.remoteauthentication}")
       private boolean remoteAuthentication;

       @Value("${tomcat.ajp.enabled}")
       private boolean tomcatAjpEnabled;

       public static void main(final String[] args) {
           SpringApplication.run(Application.class, args);
       }

       @Bean
       public EmbeddedServletContainerFactory servletContainer() {

           final TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
           if (tomcatAjpEnabled) {
               final Connector ajpConnector = new Connector("AJP/1.3");
               ajpConnector.setProtocol("AJP/1.3");
               ajpConnector.setPort(ajpPort);
               ajpConnector.setSecure(false);
               ajpConnector.setAllowTrace(false);
               ajpConnector.setScheme("http");
               ajpConnector.setAttribute("tomcatAuthentication", !remoteAuthentication);
               tomcat.addAdditionalTomcatConnectors(ajpConnector);
           }

           return tomcat;
       }
}

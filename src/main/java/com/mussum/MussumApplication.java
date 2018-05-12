package com.mussum;

import com.mussum.controllers.security.AllowFilter;
import com.mussum.controllers.security.TokenFilter;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MussumApplication {

    @Bean
    public FilterRegistrationBean getFiltroAllowOrigin() {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new AllowFilter());
        frb.addUrlPatterns("/*");
        return frb;
    }

    @Bean
    public FilterRegistrationBean getFiltroJWT() {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new TokenFilter());
        frb.addUrlPatterns("/api/*");
        return frb;
    }

    @Bean
    @Profile("test")
    public Flyway flyway(DataSource theDataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(theDataSource);
        flyway.setLocations("classpath:db/migration");
        flyway.clean();
        flyway.migrate();

        return flyway;
    }

    public static void main(String[] args) {
        SpringApplication.run(MussumApplication.class, args);
    }

}

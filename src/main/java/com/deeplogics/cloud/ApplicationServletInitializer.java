package com.deeplogics.cloud;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * ApplicationServletInitializer.java
 * This class is needed in order to initialize in the tomcat instance
 * in our remote cloud instance.
 * 
 * @author Gabriel
 *
 */
public class ApplicationServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}

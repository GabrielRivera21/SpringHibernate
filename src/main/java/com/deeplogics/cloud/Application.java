package com.deeplogics.cloud;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.deeplogics.cloud.json.ResourcesMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangofactory.swagger.plugin.EnableSwagger;

@EnableAutoConfiguration
@EnableWebMvc
@Configuration
@ComponentScan
@EnableSwagger
public class Application extends RepositoryRestMvcConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// We are overriding the bean that RepositoryRestMvcConfiguration 
	// is using to convert our objects into JSON so that we can control
	// the format. The Spring dependency injection will inject our instance
	// of ObjectMapper in all of the spring data rest classes that rely
	// on the ObjectMapper. This is an example of how Spring dependency
	// injection allows us to easily configure dependencies in code that
	// we don't have easy control over otherwise.
	@Override
	public ObjectMapper halObjectMapper(){
		return new ResourcesMapper();
	}

	//Configures the max file sizes uploaded to the server API
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("2MB");
		factory.setMaxRequestSize("2MB");
		return factory.createMultipartConfig();
	}
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/META-INF/resources/", "classpath:/resources/",
        "classpath:/static/", "classpath:/public/", "classpath:/resources/static/",
        "classpath:/resources/static/**", "classpath:/WEB-INF/**",
        "classpath:/webapp/WEB-INF/jsp/**"
    };

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}
	
	@Bean
    public UrlBasedViewResolver urlBasedViewResolver()
    {
        UrlBasedViewResolver res = new InternalResourceViewResolver();
        res.setViewClass(JstlView.class);
        res.setSuffix(".jsp");

        return res;
    }
}

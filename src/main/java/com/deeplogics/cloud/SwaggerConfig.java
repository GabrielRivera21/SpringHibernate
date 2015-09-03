package com.deeplogics.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.GenericTypeNamingStrategy;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

/**
 * This class configures the Swagger API Documentation
 * @author Gabriel
 *
 */
@Configuration
@EnableSwagger
public class SwaggerConfig {
	private SpringSwaggerConfig springSwaggerConfig;

	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	@Bean 
	public SwaggerSpringMvcPlugin customImplementation(){
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
		.apiInfo(apiInfo())
		.genericTypeNamingStrategy(new SimpleGenericNamingStrategy())
		.includePatterns("/api.*", "/users.*", "/projects.*");
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"SpringHibernate REST API",
				"SpringHibernate REST API handles all of the logic of SpringHibernate inside.",
				"You can only use this API for SpringHibernate",
				"gabriel.rivera2192@gmail.com",
				"No Licence",
				"No Licence URL"
				);
		return apiInfo;
	}

	private class SimpleGenericNamingStrategy implements GenericTypeNamingStrategy {
		private final static String OPEN = "Of";
		private final static String CLOSE = "";
		private final static String DELIM = "And";

		@Override
		public String getOpenGeneric() {
			return OPEN;
		}

		@Override
		public String getCloseGeneric() {
			return CLOSE;
		}

		@Override
		public String getTypeListDelimiter() {
			return DELIM;
		}
	}
}

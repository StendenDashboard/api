package org.eindopdracht.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.eindopdracht")
@PropertySource("classpath:application.properties") // Dit zeg je in de DatabaseConfig ook al.
public class WebConfig {

}

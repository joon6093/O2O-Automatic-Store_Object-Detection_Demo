package com.iia.store.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final MessageSource messageSource;
    private final Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = env.getProperty("upload.image.location");
        if (location != null && !location.isEmpty()) {
            registry.addResourceHandler("/image/**").addResourceLocations("file:" + location)
                    .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic());
        }
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
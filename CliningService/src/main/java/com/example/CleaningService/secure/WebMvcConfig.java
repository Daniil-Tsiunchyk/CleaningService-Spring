package com.example.CleaningService.secure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
      .addInterceptor(new AdminCheckInterceptor())
      .addPathPatterns(
        "/add-admin",
        "/add-employee",
        "/add-inventory",
        "/add-service",
        "/edit-employee",
        "/edit-inventory",
        "/edit-request",
        "/edit-service",
        "/table-employees",
        "/table-inventory",
        "/table-requests",
        "/table-services",
        "/table-users"
      );
    registry
      .addInterceptor(new UserCheckInterceptor())
      .addPathPatterns(
        "/profile",
        "/request-cleaning",
        "/user-archive",
        "/user-requests"
      );
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
      .addResourceHandler("/uploads/**")
      .addResourceLocations("file:uploads/");
  }
}

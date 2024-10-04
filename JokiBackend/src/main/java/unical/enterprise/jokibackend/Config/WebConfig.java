 package unical.enterprise.jokibackend.Config;

 import org.springframework.boot.web.servlet.FilterRegistrationBean;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.core.io.ResourceLoader;
 import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
 import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 import unical.enterprise.jokibackend.Component.CustomResourceResolver;
 import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextFilter;

 @Configuration
 public class WebConfig implements WebMvcConfigurer {

     private final ResourceLoader resourceLoader;

     public WebConfig(ResourceLoader resourceLoader) {
         this.resourceLoader = resourceLoader;
     }

     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
         registry.addResourceHandler("/images/**")
                 .addResourceLocations("classpath:/static/images/")
                 .resourceChain(true)
                 .addResolver(new CustomResourceResolver(resourceLoader));
     }

     @Bean
     public FilterRegistrationBean<UserContextFilter> userContextFilter() {
         FilterRegistrationBean<UserContextFilter> registrationBean = new FilterRegistrationBean<>();
         registrationBean.setFilter(new UserContextFilter());
         registrationBean.addUrlPatterns("/*"); // Applica il filtro a tutte le URL
         return registrationBean;
     }
 }

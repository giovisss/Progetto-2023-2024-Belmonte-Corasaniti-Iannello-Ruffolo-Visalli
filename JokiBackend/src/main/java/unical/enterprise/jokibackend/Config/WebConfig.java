 package unical.enterprise.jokibackend.Config;

 import org.springframework.boot.web.servlet.FilterRegistrationBean;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextFilter;

 @Configuration
 public class WebConfig {

     @Bean
     public FilterRegistrationBean<UserContextFilter> userContextFilter() {
         FilterRegistrationBean<UserContextFilter> registrationBean = new FilterRegistrationBean<>();
         registrationBean.setFilter(new UserContextFilter());
         registrationBean.addUrlPatterns("/*"); // Applica il filtro a tutte le URL
         return registrationBean;
     }
 }

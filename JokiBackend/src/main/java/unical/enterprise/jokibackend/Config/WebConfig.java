package unical.enterprise.jokibackend.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import unical.enterprise.jokibackend.Component.CustomResourceResolver;
import unical.enterprise.jokibackend.Component.RateLimiterInterceptor;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ResourceLoader resourceLoader;
    private final RateLimiterInterceptor rateLimiterInterceptor;

    public WebConfig(ResourceLoader resourceLoader, RateLimiterInterceptor rateLimiterInterceptor) {
        this.resourceLoader = resourceLoader;
        this.rateLimiterInterceptor = rateLimiterInterceptor;
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
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimiterInterceptor)
            .order(1)                                       // con order(1) ci si assicura che il RateLimit si applichi prima di tutto
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns(
                "/images/**",
                "/error",
                "/api/v1/chat/**"
            );
    }
}
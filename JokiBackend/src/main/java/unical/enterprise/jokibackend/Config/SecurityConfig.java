//package unical.enterprise.jokibackend.Config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.access.channel.ChannelProcessingFilter;
//import org.springframework.security.web.context.SecurityContextPersistenceFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public HttpSecurity httpSecurity() throws Exception {
//        return new HttpSecurity()
//                .anyRequest().permitAll()
//                .and()
//                .csrf().disable()
//                .headers().frameOptions().disable();
//    }
//}
//

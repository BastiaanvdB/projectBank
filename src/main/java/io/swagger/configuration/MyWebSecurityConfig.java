package io.swagger.configuration;

import io.swagger.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    // All endpoints where no authorization is necessary
    private static final String[] AUTH_ALLOWED_REQUESTS = {
            "/users/login",
            "/h2-console/**/**",
            "/swagger-ui.html",
            ".webjars/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/**/**/**"
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_ALLOWED_REQUESTS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable cross-site request forgery and set session creation policy to stateless
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Authorize all endpoints from AUTH_ALLOWED_REQUESTS list and secure all other endpoints
        http.authorizeRequests()
                .antMatchers(AUTH_ALLOWED_REQUESTS)
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // Authentication manager
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

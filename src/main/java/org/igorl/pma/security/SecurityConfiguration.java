package org.igorl.pma.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Autowired
    DataSource dataSource;

    @Autowired
    BCryptPasswordEncoder bCryptEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("select username, password, enabled from user_accounts where username = ?")
                .authoritiesByUsernameQuery("select username, role from user_accounts where username = ?")
                .dataSource(dataSource)
                .passwordEncoder(bCryptEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(new CustomerRequestCash())
                .and().authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .antMatchers("/adminpanel", "/adminpanel/**", "/actuator/**").hasAuthority("ADMIN")
                .antMatchers("/", "/**").permitAll() // .authenticated() // Dashboard must by accessible for ALL users
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and()
                .logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);

    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**");
    }
}

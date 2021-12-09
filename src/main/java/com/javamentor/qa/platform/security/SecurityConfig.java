package com.javamentor.qa.platform.security;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@NoArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private UserDetailsService userDetailsService;
    private LoginSuccessHandler loginSuccessHandler;
    private JwtAuthorizationFilter authorizationFilter;

    @Autowired
    public void setUserDetailsService(@Qualifier("userDetailsServiceImpl")
                                                  UserDetailsService userDetailsService,
                                      LoginSuccessHandler loginSuccessHandler, JwtAuthorizationFilter filter) {
        this.authorizationFilter = filter;
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/js/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.cors().disable();
        http.authorizeRequests()
                .antMatchers("/","/login","/logout","/static/**","/templates/**","/api/auth/token").permitAll()
                .antMatchers("/api/users/**").hasAuthority("ADMIN")
                .antMatchers("/api/user/**").hasAuthority("USER")

                .antMatchers("/test/admin").hasAuthority("ADMIN")
                .antMatchers("/test/user").hasAuthority("USER")

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(authorizationFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/login")
                .and().csrf().disable();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}

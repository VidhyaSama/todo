package com.microservice.todolist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * This class extends the WebSecurityConfigurerAdapter is a convenience class
 * that allows customization to both WebSecurity and HttpSecurity.<br>
 *
 * @author vidhya.sama
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SecurityScheme(name = "bearerAuth",
type = SecuritySchemeType.HTTP,
bearerFormat = "JWT",
scheme = "bearer")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private PasswordEncoder bcryptPasswordEncoder;

  @Autowired
  private UserDetailsService userService;

  @Autowired
  private JwtAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private JwtRequestFilter filter;

  /**
   * configure AuthenticationManager so that it knows from where to load.<br>
   *
   * @author vidhya.sama
   *
   */
  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth)
      throws Exception {
    // user for matching credentials
    // Use BCryptPasswordEncoder
    auth.userDetailsService(userService).passwordEncoder(bcryptPasswordEncoder);
  }

  /**
   * Override this method to expose the {@link AuthenticationManager} from
   * {@link #configure(AuthenticationManagerBuilder)} to be exposed as a Bean.
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /** Override this method to configure the HttpSecurity. */
  @Override
  protected void configure(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable().authorizeRequests()
        .antMatchers("/user/register", "/user/login", "/swagger-ui/**",
            "/todo-openapi/**" )
        .permitAll().anyRequest().authenticated().and().exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    httpSecurity.addFilterBefore(filter,
        UsernamePasswordAuthenticationFilter.class);
  }

}

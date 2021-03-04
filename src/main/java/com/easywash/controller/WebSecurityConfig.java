package com.easywash.controller;

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
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
	@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
    private AccessDeniedHandler accessDeniedHandler;
	@Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
	    return authenticationManager();
	}
	@Bean
	public UserDetailsService userDetailsService() {
	    return super.userDetailsService();
	}

	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {

	        http.csrf().disable()
	                .authorizeRequests()
	              
	                    .antMatchers("/", "/home", "/index","/resources/static/**","/Ostatus/**","/lead/**","/registration").permitAll()
	                    .antMatchers("/*.js","/*.css").permitAll()
	                    //.antMatchers("/admin/**").hasAnyRole("ADMIN")
	                    //.antMatchers("/home/**").hasAnyRole("ADMIN")
	                   // .antMatchers("/user/**").hasAnyRole("USER")
	                    .anyRequest().authenticated()
	                .and()
					
					  .formLogin() .loginPage("/login") .defaultSuccessUrl("/home/1").permitAll() .and()
					 
	                .logout()
	                    .permitAll()
	                    .and()
	                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	    }
   
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/bootstrap/**", "/fonts/**");
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
		
		/*
		 * auth.inMemoryAuthentication()
		 * .withUser("admin").password(bCryptPasswordEncoder().encode("admin")).roles(
		 * "ADMIN").and()
		 * .withUser("user").password(bCryptPasswordEncoder().encode("user")).roles(
		 * "USER");
		 */
		 


    }
	
		
	  
	    
	    

}

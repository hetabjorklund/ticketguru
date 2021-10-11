package fi.paikalla.ticketguru.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class WebSecConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	//private UserDetailServiceImp serviceImp; 
	
	@Override
	protected void configure (HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/index","/").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			//.loginPage("").permitAll()
			.defaultSuccessUrl("/index", true)
			.permitAll()
			.and()
		.logout()
			.logoutSuccessUrl("/index")
			.permitAll();
		}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(serviceImp).passwordEncoder(new BCryptPasswordEncoder());
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	            .ignoring()
	            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}

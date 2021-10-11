package fi.paikalla.ticketguru.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fi.paikalla.ticketguru.Services.UserDetailsServiceImplementation;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled= true)
@EnableWebSecurity
public class WebSecConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsServiceImplementation serviceImp; 
	
	@Override
	protected void configure (HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.httpBasic()
			.and()
			.csrf().disable(); 
		}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(serviceImp).passwordEncoder(new BCryptPasswordEncoder());
		auth
		.inMemoryAuthentication()
		.withUser("user").password("pssword")
		.authorities("USER"); 
    }
	
	

}

package fi.paikalla.ticketguru.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import fi.paikalla.ticketguru.Components.UserDetailsServiceImplementation;
import fi.paikalla.ticketguru.Entities.TGUser;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled= true, securedEnabled = true) //eli metoditasolla @PreAuthorize("hasRole('USER')") tai @PreAuthorize("hasAnyRole('ADMIN','USER')"), kts. https://www.baeldung.com/spring-security-method-security
@EnableWebSecurity
public class WebSecConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImplementation serviceImp; 
	
	@Override
	protected void configure (HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.anyRequest().authenticated()//kaiken pitää olla autorisoitua
			.and()
			.httpBasic() //http Basic protokollalla
			.and()
			.csrf().disable(); //eikä mitään sessioita 
		}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
        auth.userDetailsService(serviceImp).passwordEncoder(TGUser.PASSWORD_ENCODER); //sitten kun tätä tarvitaan 
		
        // lisätään user-tasoinen käyttäjä (käytä näitä tunnuksia Postmanissa)
        
		auth
		.inMemoryAuthentication()  
		.withUser("user").password(passCoder().encode("password"))
		.authorities("ROLE_USER").roles("USER");
		
		// lisätään admin-tasoinen käyttäjä (käytä näitä tunnuksia Postmanissa)
		auth
        .inMemoryAuthentication() 
        .withUser("admin").password(passCoder().encode("password"))
        .authorities("ROLE_ADMIN").roles("ADMIN");
		
    }    

	
	@Bean
	public PasswordEncoder passCoder() {
		return new BCryptPasswordEncoder(); 
	}
	
	

}

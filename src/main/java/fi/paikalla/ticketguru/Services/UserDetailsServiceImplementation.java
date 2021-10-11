package fi.paikalla.ticketguru.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fi.paikalla.ticketguru.Entities.TGUser;
import fi.paikalla.ticketguru.Repositories.TGUserRepository;

public class UserDetailsServiceImplementation implements UserDetailsService {
	
	private TGUserRepository urepo; 
	
	@Autowired
	public void UserDetailServiceImp(TGUserRepository repository) {
		this.urepo = repository;
	}   
	
	 @Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   
	    	TGUser curruser = urepo.findByUserName(username);
	        UserDetails user = new User(username, curruser.getPassword(), 
	        		AuthorityUtils.createAuthorityList(curruser.getAuth()));
	        return user;
	    }

}

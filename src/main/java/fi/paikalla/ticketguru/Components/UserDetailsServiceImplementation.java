package fi.paikalla.ticketguru.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import fi.paikalla.ticketguru.Entities.TGUser;
import fi.paikalla.ticketguru.Repositories.TGUserRepository;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {
	
	@Autowired
	private TGUserRepository userRepo; 
	
	/*@Autowired
	public void UserDetailServiceImp(TGUserRepository repository) {
		this.userRepo = repository;
	} */  // kommentoitu pois koska riittää että oliomuuttuja on Autowired
	
	 @Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   
	    	TGUser curruser = userRepo.findByUserName(username);
	    	
	    	if (curruser == null) {
	            throw new UsernameNotFoundException(username + " was not found");
	    	}
	            
	        UserDetails user = new User(curruser.getUserName(), curruser.getPassword(), 
	        		AuthorityUtils.createAuthorityList(curruser.getAuth()));
	        return user;
	    }

}

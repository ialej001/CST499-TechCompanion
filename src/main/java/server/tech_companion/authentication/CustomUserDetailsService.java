package server.tech_companion.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import server.tech_companion.models.User;
import server.tech_companion.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository users;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = users.findByUsername(username); 
		
		if (user != null) {
			return CustomUserDetails.build(user);
		} else {
			throw new UsernameNotFoundException("Username: " + username + " not found");
		}
	}
}

package com.deeplogics.cloud.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.deeplogics.cloud.model.Role;
import com.deeplogics.cloud.model.Users;
import com.deeplogics.cloud.repositories.UsersRepository;


/**
 * This is the UserDetailsService of this Spring Application.
 * This class is used to retrieve our Users from our Database and
 * validate them in our OAuth 2.0 Security.
 * 
 * @author Gabriel
 *
 */
@Component
public class SpringHibernateUserDetailsService implements UserDetailsService{

	@Autowired
	private UsersRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Users customUser = userRepository.findByEmail(email); 

		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;

		return new User(
				customUser.getId(),
				customUser.getPassword(),
				customUser.isEnabled(),
				accountNonExpired,
				credentialsNonExpired,
				customUser.isAccountNonLocked(),
				getAuthorities(customUser.getRoles()));
	}

	/**
	 * 
	 * @param roles
	 * @return
	 */
	private Collection<GrantedAuthority> getAuthorities(Collection<Role> roles) {
		Collection<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		
		for (Role role : roles) {
			authList.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		
		return authList;
	}

}

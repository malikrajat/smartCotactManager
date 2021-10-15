package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userByUserName = userRepo.getUserByUserName(username);
		if(userByUserName == null) {
			throw new UsernameNotFoundException("Could not found user !!");
		}
		CustomUserDetail customUserDetail = new CustomUserDetail(userByUserName);
		return customUserDetail;
	}
	
}

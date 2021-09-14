package com.main.videosapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.main.videosapi.entity.Partner;
import com.main.videosapi.repository.PartnerRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	PartnerRepository partnerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Partner> user = partnerRepository.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("User Not Found - " + username));

		return user.map(MyUserDetails::new).get();
	}

}

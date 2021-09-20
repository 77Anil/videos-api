package com.main.videosapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.main.videosapi.service.MyUserDetailsService;
import com.main.videosapi.util.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	MyUserDetailsService userDeatilsService;

	@Autowired
	JwtUtils jwt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, ExpiredJwtException {

		final String authorizationHeader = request.getHeader("Authorization");
		String token = null, username = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			try {
				username = jwt.extractUsername(token);
			} catch (ExpiredJwtException ex) {
				request.setAttribute("expired", ex.getMessage());
			}

		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails user = userDeatilsService.loadUserByUsername(username);

			if (jwt.validateToken(token, user)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}

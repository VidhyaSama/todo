package com.microservice.todolist.config;

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

import com.microservice.todolist.service.UserService;
import com.microservice.todolist.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * For any incoming request this Filter class gets executed.<br>
 *
 * @author vidhya.sama
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  /**
  *
  * This method is invoked once per request.
  * @param httpServletRequest that contains Authorization token.
  * @param httpServletResponse the response to pass along the chains.
  *
  */
  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain)
      throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;
    final int tokenStarts = 7;
    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null
        && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(tokenStarts);
      try {
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      }
    } else {
      logger.warn("JWT Token does not begin with Bearer String");
    }

    // Once we get the token validate it.
    if (username != null
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userService.loadUserByUsername(username);
      // if token is valid configure Spring Security to manually set
      // authentication
      if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new
            UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext()
            .setAuthentication(authToken);
      }
    }
    chain.doFilter(request, response);
  }

}

package com.lzg.takeout.config;

import com.lzg.takeout.service.impl.CustomUserDetailsService;
import com.lzg.takeout.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = parseToken(request);
            if (token != null) {
                logger.info("JwtAuthenticationFilter - Token found: {}", token);
                if (jwtUtils.isTokenValid(token)) {
                    logger.info("JwtAuthenticationFilter - Token is valid");
                    String username = jwtUtils.getUsername(token);
                    logger.info("JwtAuthenticationFilter - Username extracted from token: {}", username);

                    var userDetails = userDetailsService.loadUserByUsername(username);
                    logger.info("JwtAuthenticationFilter - Loaded userDetails for: {}", userDetails.getUsername());

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("JwtAuthenticationFilter - Authentication set in SecurityContextHolder");
                } else {
                    logger.warn("JwtAuthenticationFilter - Invalid JWT token");
                }
            } else {
                logger.info("JwtAuthenticationFilter - No JWT token found in request");
            }
        } catch (Exception e) {
            logger.error("JwtAuthenticationFilter - Could not set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}

package ies.g25.aLIVE.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import ies.g25.aLIVE.service.JwtTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import ies.g25.aLIVE.repository.UserRepository;
import ies.g25.aLIVE.service.JwtTokenService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        //this.setAuthenticationManager(authenticationManager);
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/login");
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        Claims claims = new DefaultClaims();
        HashMap<String, Object> responseBody = new HashMap<>();

        String username = ((UserDetails) authResult.getPrincipal()).getUsername();
        String r = this.userRepository.findByUsername(username).get().getRole();
        Long id = this.userRepository.findByUsername(username).get().getId();
        
        List<String> authorities = authResult.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());
        claims.put("authorities", authorities);

        String token = JwtTokenService.generateToken(username, claims);

        responseBody.put("token", token);
        responseBody.put("role", r);
        responseBody.put("id", id);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));
    }
}

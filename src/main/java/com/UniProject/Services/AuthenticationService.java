package com.UniProject.Services;

import com.UniProject.DTO.JwtResponse;
import com.UniProject.DTO.RefreshToken;
import com.UniProject.DTO.SignInRequest;
import com.UniProject.Entities.User;
import com.UniProject.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class AuthenticationService {


    //private final AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private UserService service;

    public JwtResponse login(SignInRequest signInRequest){
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
        UserDetails user=userService.loadUserByUsername(signInRequest.getEmail());
        User checkUser=service.checkUser(signInRequest.getEmail(),signInRequest.getPassword());
        if(user==null || checkUser==null){
            throw new IllegalArgumentException("Wrong username or password");
        }
        System.out.println(user);
        String jwt=jwtService.generateToken(user);
        String refreshJwt=jwtService.generateRefreshedToken(new HashMap<>(),user);

        JwtResponse jwtResponse=new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setRefreshToken(refreshJwt);
        return jwtResponse;
    }
    public JwtResponse refreshToken(RefreshToken token){
        String username=jwtService.extractUsername(token.getRefreshToken());
        System.out.println(token.getRefreshToken()+" "+username);
        UserDetails user=userService.loadUserByUsername(username);
        if(user==null){
            throw new IllegalArgumentException("Email not found");
        }
        if(jwtService.isTokenValid(token.getRefreshToken(),user)){
            String jwt=jwtService.generateToken(user);
            String refreshToken=jwtService.generateRefreshedToken(new HashMap<>(),user);

            JwtResponse jwtResponse=new JwtResponse();

            jwtResponse.setToken(jwt);
            jwtResponse.setRefreshToken(refreshToken);
            return jwtResponse;
        }
        return null;
    }
}

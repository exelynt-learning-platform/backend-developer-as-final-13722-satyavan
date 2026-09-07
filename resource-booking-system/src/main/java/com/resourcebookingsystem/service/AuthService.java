package com.resourcebookingsystem.service;

import com.resourcebookingsystem.dto.AuthResponse;
import com.resourcebookingsystem.dto.LoginRequest;
import com.resourcebookingsystem.dto.RegisterRequest;
import com.resourcebookingsystem.model.Role;
import com.resourcebookingsystem.model.User;
import com.resourcebookingsystem.repository.UserRepository;
import com.resourcebookingsystem.security.JwtTokenProvider;
import com.resourcebookingsystem.security.TokenBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlackListService blackListService;

    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email is already registered");
        }
        //New registrations always default to ROLE_USER for security
        User user=User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        // Generate token immediately so user is logged in upon registration
        Authentication auth=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        String token =tokenProvider.generateToken(auth);
        return new AuthResponse(token,user.getEmail(),user.getRole().name());
    }

    public AuthResponse login(LoginRequest request){
        Authentication auth =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        User user=(User) auth.getPrincipal();
        String token=tokenProvider.generateToken(auth);
        return new AuthResponse(token,user.getEmail(),user.getRole().name());
    }

    public void logout(String token){
        Date expiration=tokenProvider.getExpirationDateFromToken(token);
        blackListService.blacklistToken(token,expiration);
    }

}

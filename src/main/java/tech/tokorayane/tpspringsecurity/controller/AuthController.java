package tech.tokorayane.tpspringsecurity.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.tokorayane.tpspringsecurity.dto.AuthResponse;
import tech.tokorayane.tpspringsecurity.dto.LoginUserRequest;
import tech.tokorayane.tpspringsecurity.dto.RegisterUserRequest;
import tech.tokorayane.tpspringsecurity.entity.AppUser;
import tech.tokorayane.tpspringsecurity.repository.UserRepository;
import tech.tokorayane.tpspringsecurity.service.CustomUserDetailsService;
import tech.tokorayane.tpspringsecurity.service.JwtService;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.email())) {
            return ResponseEntity.badRequest().body("Utilisateur deja cree");
        }

        AppUser user = new AppUser();
        user.setEmail(registerUserRequest.email());
        user.setPassword(passwordEncoder.encode(registerUserRequest.password()));
        user.setRole("USER");
        userRepository.save(user);
        return ResponseEntity.ok("Enregistre avec succes");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginUserRequest loginUserRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginUserRequest.email(),
                loginUserRequest.password()
        ));

        UserDetails user = customUserDetailsService.loadUserByUsername(loginUserRequest.email());
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}

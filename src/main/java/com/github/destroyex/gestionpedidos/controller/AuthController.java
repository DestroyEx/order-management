package com.github.destroyex.gestionpedidos.controller;

import com.github.destroyex.gestionpedidos.dao.UserRepository;
import com.github.destroyex.gestionpedidos.dto.LoginRequestDTO;
import com.github.destroyex.gestionpedidos.dto.LoginResponseDTO;
import com.github.destroyex.gestionpedidos.dto.RegisterRequestDTO;
import com.github.destroyex.gestionpedidos.entity.User;
import com.github.destroyex.gestionpedidos.exception.InvalidCredentialsException;
import com.github.destroyex.gestionpedidos.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponseDTO(token);
    }

    @PostMapping("/register")
    public LoginResponseDTO register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(registerRequestDTO.getRole());

        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponseDTO(token);
    }
}

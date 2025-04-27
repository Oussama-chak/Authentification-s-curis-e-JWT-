package cpu_jwt.services.security.Impl;

import cpu_jwt.DTO.JwtAuthenticationResponse;
import cpu_jwt.DTO.LoginRequest;
import cpu_jwt.DTO.SignUpRequest;
import cpu_jwt.entities.Role;
import cpu_jwt.entities.User;
import cpu_jwt.repos.UserRepo;
import cpu_jwt.services.security.AuthenticationService;
import cpu_jwt.services.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public JwtAuthenticationResponse SignUp(SignUpRequest request, HttpServletResponse response) {
        // Set default role to USER if no role is provided
        Role role = request.getRole() != null ? request.getRole() : Role.USER;

        // Create the user with the default role
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(role)
                .build();

        // Save the user to the database
        userRepository.save(user);

        // Generate JWT token for the new user
        var jwt = jwtService.generateToken(user);

        // Store JWT in an HttpOnly cookie
        setJwtInCookie(jwt, response);

        // Return a response containing user info but not the token directly
        return JwtAuthenticationResponse.builder()
                .userId(user.getId()) // Set the user ID
                .role(user.getRole().name()) // Set the role
                .build();
    }

    @Override
    public JwtAuthenticationResponse Signin(LoginRequest request, HttpServletResponse response) {
        // Authenticate the user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Retrieve the user from the database
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // Generate JWT token for the authenticated user
        var jwt = jwtService.generateToken(user);

        // Store JWT in an HttpOnly cookie
        setJwtInCookie(jwt, response);

        // Return a response containing user info but not the token directly
        return JwtAuthenticationResponse.builder()
                .userId(user.getId()) // Set the user ID
                .role(user.getRole().name()) // Set the role
                .build();
    }



    private void setJwtInCookie(String jwt, HttpServletResponse response) {
        // Create HttpOnly cookie to store JWT token
        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(true); // Ensure the cookie is HttpOnly
        cookie.setSecure(true); // Set to true for HTTPS connections (use in production)
        cookie.setPath("/"); // Cookie is available for the entire domain
        cookie.setMaxAge(3600); // 1 hour expiration time

        // Add the cookie to the response
        response.addCookie(cookie);
    }}
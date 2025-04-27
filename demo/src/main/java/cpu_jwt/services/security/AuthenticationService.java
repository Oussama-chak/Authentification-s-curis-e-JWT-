package cpu_jwt.services.security;

import cpu_jwt.DTO.JwtAuthenticationResponse;
import cpu_jwt.DTO.LoginRequest;
import cpu_jwt.DTO.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse SignUp(SignUpRequest request, HttpServletResponse response);
    JwtAuthenticationResponse Signin(LoginRequest request, HttpServletResponse response);



}
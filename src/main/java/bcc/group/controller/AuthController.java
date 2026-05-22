package bcc.group.controller;

import bcc.group.dto.security.AuthResponse;
import bcc.group.dto.security.LoginRequest;
import bcc.group.dto.security.RefreshTokenRequest;
import bcc.group.dto.security.SignupRequest;
import bcc.group.entity.User;
import bcc.group.repository.UserRepository;
import bcc.group.security.CustomUserDetailsService;
import bcc.group.security.JwtService;
import bcc.group.services.impl.OtpService;
import bcc.group.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j  //  Use proper logging
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final OtpService otpService;

    // ─── SIGNUP ──────────────────────────────────────────────
    @PostMapping("/signup/request-otp")
    public ResponseEntity<ApiResponse<String>> requestSignupOtp(@Valid @RequestBody SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email already registered", null));
        }
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Username already taken", null));
        }

        otpService.sendOtp(request.email());
        return ResponseEntity.ok(new ApiResponse<>(true, "OTP sent to your email. Please verify.", null));
    }

    @PostMapping("/signup/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifySignupOtp(
            @RequestBody Map<String, String> otpRequest) {

        String email = otpRequest.get("email");
        String otp = otpRequest.get("otp");
        String username = otpRequest.get("username");
        String password = otpRequest.get("password");

        log.debug("Verify OTP: email={}, username={}", email, username);

        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Invalid or expired OTP", null));
        }

        //  Validation: Password required
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Password is required", null));
        }

        //  Validation: Username required
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Username is required", null));
        }

        //  Actual username & password from frontend, default ROLE_USER
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Set.of("ROLE_USER")) //  Safe default
                .build();
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(username, Map.of("roles", user.getRoles().stream().toList()));
        String refreshToken = jwtService.generateRefreshToken(username);

        AuthResponse response = new AuthResponse(accessToken, refreshToken, username, "Signup successful");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Account created successfully", response));
    }

    // ─── LOGIN ──────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt: {}", request.username());

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(request.username());
            log.debug("User found: {}", userDetails.getUsername());
        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", request.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid credentials", null));
        }

        boolean matches = passwordEncoder.matches(request.password(), userDetails.getPassword());
        log.debug("Password match: {}", matches);

        if (!matches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid credentials", null));
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        log.debug("Sending OTP to: {}", user.getEmail());

        otpService.sendOtp(user.getEmail());

        return ResponseEntity.ok(new ApiResponse<>(true,
                "OTP sent to your email. Please verify to complete login.",
                Map.of("email", user.getEmail())));
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyLoginOtp(
            @RequestBody Map<String, String> otpRequest) {
        String email = otpRequest.get("email");
        String otp = otpRequest.get("otp");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Invalid or expired OTP", null));
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername(),
                Map.of("roles", user.getRoles().stream().toList()));
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        AuthResponse response = new AuthResponse(accessToken, refreshToken, user.getUsername(), "Login successful");
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged in successfully", response));
    }

    // ─── REFRESH TOKEN ──────────────────────────────────────
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            String username = jwtService.extractUsername(request.refreshToken());
            if (username == null || !jwtService.isTokenValid(request.refreshToken(), username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid refresh token", null));
            }
            String newAccessToken = jwtService.generateAccessToken(username, null);
            AuthResponse response = new AuthResponse(newAccessToken, request.refreshToken(), username, "Token refreshed");
            return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid refresh token", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out", null));
    }
}
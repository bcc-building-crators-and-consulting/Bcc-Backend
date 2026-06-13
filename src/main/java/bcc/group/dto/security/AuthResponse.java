package bcc.group.dto.security;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String username,
        String message
) {}
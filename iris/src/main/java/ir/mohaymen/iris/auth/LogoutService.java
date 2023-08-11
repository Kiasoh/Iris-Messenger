package ir.mohaymen.iris.auth;

import ir.mohaymen.iris.token.Token;
import ir.mohaymen.iris.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        final String jwt = authHeader.substring(7);
        var phoneNumber = jwtService.extractUsername(jwt);
        var tokens = tokenRepository.findAllValidTokenByUserPhoneNumber(phoneNumber);
        for (Token token : tokens) {
            token.setExpiresAt(Instant.now());
        }
        tokenRepository.saveAll(tokens);
        SecurityContextHolder.clearContext();
    }
}

package bcc.group.security;

import bcc.group.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Pehle email se try karo
        return userRepository.findByEmail(emailOrUsername)
                .map(UserPrincipal::new)
                .orElseGet(() -> {
                    // Agar email se nahi mila to username se try karo
                    return userRepository.findByUsername(emailOrUsername)
                            .map(UserPrincipal::new)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found with email/username: " + emailOrUsername));
                });
    }
}
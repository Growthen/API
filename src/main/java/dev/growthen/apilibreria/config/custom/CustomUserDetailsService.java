package dev.growthen.apilibreria.config.custom;

import dev.growthen.apilibreria.common.constants.ErrorMessages;
import dev.growthen.apilibreria.user.entity.User;
import dev.growthen.apilibreria.user.repository.UserRepository;
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
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                User user = userRepository
                                .findByUsernameAndIsDeletedFalse(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                ErrorMessages.USER_NOT_FOUND));

                return org.springframework.security.core.userdetails.User
                                .builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole().name())
                                .build();
        }
}

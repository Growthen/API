package dev.growthen.api.config.custom;

import dev.growthen.api.common.constants.ErrorMessages;
import dev.growthen.api.user.entity.User;
import dev.growthen.api.user.repository.UserRepository;
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

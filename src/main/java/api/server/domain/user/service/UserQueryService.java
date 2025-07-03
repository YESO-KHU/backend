package api.server.domain.user.service;

import api.server.common.payload.failure.customException.UserException;
import api.server.domain.user.dto.request.LoginRequest;
import api.server.domain.user.entity.User;
import api.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserQueryService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String checkUserInfo(LoginRequest loginRequest){
        log.info("[UserQueryService - login]");

        // username 확인
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(UserException.UsernameNotExistException::new);

        // password 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new UserException.PasswordNotValidException();
        }

        return user.getUsername();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 ID: " + userId));
    }
}


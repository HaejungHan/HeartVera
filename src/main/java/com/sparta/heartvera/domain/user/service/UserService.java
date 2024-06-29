package com.sparta.heartvera.domain.user.service;

import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import com.sparta.heartvera.domain.user.dto.UserPwRequestDto;
import com.sparta.heartvera.domain.user.dto.UserRequestDto;
import com.sparta.heartvera.domain.user.dto.UserResponseDto;
import com.sparta.heartvera.domain.user.entity.PasswordHistory;
import com.sparta.heartvera.domain.user.entity.User;
import com.sparta.heartvera.domain.user.entity.UserRoleEnum;
import com.sparta.heartvera.domain.user.repository.PasswordHistoryRepository;
import com.sparta.heartvera.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sparta.heartvera.common.exception.ErrorCode.USER_NOT_UNIQUE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LikeRepository likeRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    // 사용자 프로필 조회
    public UserResponseDto getUser(Long userSeq) {
        User user = findByUserSeq(userSeq);
        int likedPostCount = likeRepository.getLikedPostCount(userSeq);
        int likedCommentCount = likeRepository.getLikedCommentCount(userSeq);
        return new UserResponseDto(user, likedPostCount, likedCommentCount);
    }

    // 사용자 프로필 수정
    @Transactional
    public UserResponseDto updateUser(UserRequestDto requestDto, Long userSeq) {
        User user = findByUserSeq(userSeq);
        user.updateUser(requestDto);
        return new UserResponseDto(user);
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(UserPwRequestDto requestDto, Long userSeq) {
        User user = findByUserSeq(userSeq);
        String password = requestDto.getPassword();
        String newPassword = requestDto.getNewPassword();

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new CustomException(ErrorCode.CURRENT_PASSWORD_MATCH);
        }

        if (passwordEncoder.matches(newPassword, user.getUserPassword())) {
            throw new CustomException(ErrorCode.SAME_NEW_PASSWORD);
        }

        List<PasswordHistory> usedPasswords = passwordHistoryRepository.findTop3ByUserOrderByChangedAtDesc(
                user);
        for (PasswordHistory usedPassword : usedPasswords) {
            if (passwordEncoder.matches(newPassword, usedPassword.getPassword())) {
                throw new CustomException(ErrorCode.RECENT_PASSWORD_MATCH);
            }
        }

        if (usedPasswords.size() >= 3) {
            PasswordHistory oldPassword = usedPasswords.get(usedPasswords.size() - 1);
            passwordHistoryRepository.delete(oldPassword);
        }

        PasswordHistory newPasswordHistory = new PasswordHistory(user, passwordEncoder.encode(newPassword), LocalDateTime.now());
        passwordHistoryRepository.save(newPasswordHistory);

        user.updatePassword(passwordEncoder.encode(newPassword));

    }

    @Transactional
    public User createUser(String userId, String userName,
                           String password, String email,
                           String description, UserRoleEnum role) {
        // 유저 Entity 생성
        User user = User.builder()
                .userId(userId)
                .userName(userName)
                .userPassword(password)
                .userEmail(email)
                .description(description)
                .authority(role)
                .build();

        // 유저 DB 생성
        userRepository.save(user);

        return user;
    }

    @Transactional
    public void logout(User user) {
        // 유저의 리프레쉬 토큰 초기화
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User findByUserSeq(Long userSeq) {
        return userRepository.findById(userSeq).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

  public List<User> findAllUser() {
    return userRepository.findAll();
  };
    public void findByUserId(String userName) {
        Optional<User> existingUser = userRepository.findByUserId(userName);
        if (existingUser.isPresent()) {
            throw new CustomException(USER_NOT_UNIQUE);
        }
    }

    public User findByRefreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        return user;
    }

}

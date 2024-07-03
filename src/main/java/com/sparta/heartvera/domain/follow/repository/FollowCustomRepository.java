package com.sparta.heartvera.domain.follow.repository;

import com.sparta.heartvera.domain.user.dto.UserResponseDto;
import com.sparta.heartvera.domain.user.dto.UserTop10ResponseDto;
import com.sparta.heartvera.domain.user.entity.User;
import java.util.List;

public interface FollowCustomRepository {
  List<Long> findFollowedUserIds(Long userId);
  List<UserTop10ResponseDto> findTop10Followers();
}

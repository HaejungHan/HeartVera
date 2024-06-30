package com.sparta.heartvera.domain.follow.repository;

import java.util.List;

public interface FollowCustomRepository {
  List<Long> findFollowedUserIds(Long userId);
}

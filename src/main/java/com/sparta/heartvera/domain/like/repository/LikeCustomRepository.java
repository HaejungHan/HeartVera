package com.sparta.heartvera.domain.like.repository;

import com.sparta.heartvera.domain.like.entity.LikeEnum;
import java.util.List;

public interface LikeCustomRepository {

  int getLikesCount(Long contentId, LikeEnum contentType);
  List<Long> getLikedPostIds(Long userId);
  List<Long> getLikedPublicPostIds(Long userId);
  List<Long> getLikedCommentIds(Long userId);
  int getLikedPostCount(Long userId);
  int getLikedCommentCount(Long userId);
}

package com.sparta.heartvera.domain.post.repository;

import com.sparta.heartvera.domain.post.entity.PublicPost;
import java.util.List;

public interface PublicPostCustomRepository {

  PublicPost findByPublicPostId(Long postId);
  List<PublicPost> findByPublicPostId(int page, int pageSize, List<Long> likedPostIds);
  List<PublicPost> findByPublicPostIdOrderByCreatedAt(int page, int pageSize, List<Long> followedUserIds);
  List<PublicPost> findByPublicPostIdOrderByUsername(int page, int pageSize, List<Long> followedUserIds);
}

package com.sparta.heartvera.domain.post.repository;

import com.sparta.heartvera.domain.post.entity.PublicPost;

public interface PublicPostCustomRepository {

  PublicPost findByPublicPostId(Long postId);
}

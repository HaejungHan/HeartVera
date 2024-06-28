package com.sparta.heartvera.domain.post.repository;

import com.sparta.heartvera.domain.post.entity.Post;

public interface PostCustomRepository {

 Post findByPostId(Long postId);
}

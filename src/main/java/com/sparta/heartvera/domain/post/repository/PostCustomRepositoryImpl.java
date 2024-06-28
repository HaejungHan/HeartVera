package com.sparta.heartvera.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.entity.QPost;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

   private final JPAQueryFactory jpaQueryFactory;

  public Post findByPostId(Long postId) {
    QPost post = QPost.post;
    return jpaQueryFactory
        .selectFrom(QPost.post)
        .where(QPost.post.id.eq(postId))
        .fetchOne();
  }
}

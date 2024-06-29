package com.sparta.heartvera.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

   private final JPAQueryFactory jpaQueryFactory;

  public Post findByPostId(Long postId) {
    QPost qPost = QPost.post;
    try {
      Post post = jpaQueryFactory
          .selectFrom(qPost)
          .where(qPost.id.eq(postId))
          .fetchOne();
      return post;
    } catch (Exception e) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }
  }
}

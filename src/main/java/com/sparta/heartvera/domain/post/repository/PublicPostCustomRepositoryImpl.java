package com.sparta.heartvera.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.post.entity.PublicPost;
import com.sparta.heartvera.domain.post.entity.QPublicPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PublicPostCustomRepositoryImpl implements PublicPostCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PublicPost findByPublicPostId(Long postId) {
    QPublicPost QpublicPost = QPublicPost.publicPost;
    try {
      PublicPost publicPost = jpaQueryFactory
          .selectFrom(QPublicPost.publicPost)
          .where(QPublicPost.publicPost.id.eq(postId))
          .fetchOne();
      return publicPost;
    } catch (Exception e) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }
  }
}

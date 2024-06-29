package com.sparta.heartvera.domain.like.repository;

import static com.sparta.heartvera.domain.like.entity.QLike.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import com.sparta.heartvera.domain.like.entity.QLike;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeCustomRepositoryImpl implements LikeCustomRepository{

 private final JPAQueryFactory jpaQueryFactory;

  public int getLikesCount(Long contentId, LikeEnum contentType) {
    QLike like = QLike.like;
    return (int) jpaQueryFactory
        .selectFrom(QLike.like)
        .where(QLike.like.contentId.eq(contentId).and(QLike.like.contentType.eq(contentType)))
        .fetchCount();
  }

  public List<Long> getLikedPostIds(Long userId) {
    QLike like = QLike.like;
    return jpaQueryFactory
        .select(like.contentId)
        .from(like)
        .where(like.userId.eq(userId).and(like.contentType.eq(LikeEnum.POST)))
        .fetch();
  }

  public List<Long> getLikedPublicPostIds(Long userId) {
    QLike like = QLike.like;
    return jpaQueryFactory
        .select(like.contentId)
        .from(like)
        .where(like.userId.eq(userId).and(like.contentType.eq(LikeEnum.PUBPOST)))
        .fetch();
  }

  public int getLikedPostCount(Long userId) {
    QLike like = QLike.like;
    return (int) jpaQueryFactory
        .selectFrom(QLike.like)
        .where(QLike.like.userId.eq(userId).and(QLike.like.contentType.eq(LikeEnum.POST)))
        .fetchCount();
  }

  public int getLikedCommentCount(Long userId) {
    QLike like = QLike.like;
    return (int) jpaQueryFactory
        .selectFrom(QLike.like)
        .where(QLike.like.userId.eq(userId).and(QLike.like.contentType.eq(LikeEnum.COMMENT)))
        .fetchCount();
  }

}

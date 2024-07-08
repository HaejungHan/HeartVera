package com.sparta.heartvera.domain.follow.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.domain.follow.entity.QFollow;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import com.sparta.heartvera.domain.like.entity.QLike;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import com.sparta.heartvera.domain.user.dto.UserResponseDto;
import com.sparta.heartvera.domain.user.dto.UserTop10ResponseDto;
import com.sparta.heartvera.domain.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class FollowCustomRepositoryImpl implements FollowCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final LikeRepository likeRepository;

  @Transactional(readOnly = true)
  public List<Long> findFollowedUserIds(Long userId) {
    QFollow follow = QFollow.follow;

    List<Long> followedUserIds = jpaQueryFactory
        .select(follow.toUser.userSeq)
        .from(follow)
        .where(follow.fromUser.userSeq.eq(userId))
        .fetch();

    return followedUserIds;
  }

  @Transactional(readOnly = true)
  public List<UserTop10ResponseDto> findTop10Followers() {
    QFollow follow = QFollow.follow;
    QUser user = QUser.user;
    QLike like = QLike.like;

    List<UserTop10ResponseDto> topFollowers = jpaQueryFactory
        .select(Projections.constructor(UserTop10ResponseDto.class,
            user.userId,
            user.userName,
            user.userEmail,
            user.description,
            follow.toUser.count().intValue().as("followCount")))
        .from(follow)
        .leftJoin(follow.toUser, user)
        .groupBy(user.userId, user.userName, user.userEmail, user.description)
        .orderBy(follow.toUser.count().intValue().desc())
        .limit(10)
        .fetch();
    return topFollowers;
  }
}

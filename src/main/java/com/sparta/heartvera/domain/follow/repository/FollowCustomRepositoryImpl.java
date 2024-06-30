package com.sparta.heartvera.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.domain.follow.entity.QFollow;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowCustomRepositoryImpl implements FollowCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public List<Long> findFollowedUserIds(Long userId) {
    QFollow follow = QFollow.follow;

    List<Long> followedUserIds = jpaQueryFactory
        .select(follow.toUser.userSeq)
        .from(follow)
        .where(follow.fromUser.userSeq.eq(userId))
        .fetch();

    return followedUserIds;
  }
}

package com.sparta.heartvera.domain.post.repository;

import static com.sparta.heartvera.domain.post.entity.QPublicPost.publicPost;
import static com.sparta.heartvera.domain.user.entity.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.post.entity.PublicPost;
import com.sparta.heartvera.domain.post.entity.QPublicPost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PublicPostCustomRepositoryImpl implements PublicPostCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PublicPost findByPublicPostId(Long postId) {
    QPublicPost qPublicPost = QPublicPost.publicPost;

      PublicPost publicPost = jpaQueryFactory
          .selectFrom(QPublicPost.publicPost)
          .where(QPublicPost.publicPost.id.eq(postId))
          .fetchOne();
      return publicPost;

  }

  public List<PublicPost> findByPublicPostId(int page, int pageSize, List<Long> likedPostIds) {
    Pageable pageable = PageRequest.of(page, pageSize);
    QPublicPost qPublicPost = QPublicPost.publicPost;

      List<PublicPost> publicPostList = jpaQueryFactory
          .selectFrom(publicPost)
          .where(publicPost.id.in(likedPostIds))
          .orderBy(publicPost.createdAt.desc())
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();
      return publicPostList;

  }

  public List<PublicPost> findByPublicPostId(int page, int pageSize, List<Long> followedUserIds, String orderBy) {
    Pageable pageable = PageRequest.of(page, pageSize);
    QPublicPost qPublicPost = QPublicPost.publicPost;
    OrderSpecifier<?> orderSpecifier = null;

    if ("createdAt".equals(orderBy)) {
      orderSpecifier = publicPost.createdAt.desc();
    } else if ("username".equals(orderBy)) {
      orderSpecifier = publicPost.user.userName.asc();
    } else {
      throw new CustomException(ErrorCode.POST_ORDER_ERROR);
    }

    List<PublicPost> publicPostList = jpaQueryFactory
          .selectFrom(publicPost)
          .where(publicPost.user.userSeq.in(followedUserIds))
          .orderBy(orderSpecifier)
          .offset(pageable.getOffset())
          .limit(pageable.getPageSize())
          .fetch();

      return publicPostList;

  }

}

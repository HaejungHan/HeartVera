package com.sparta.heartvera.domain.post.service;

import static com.sparta.heartvera.domain.like.entity.QLike.like;
import static com.sparta.heartvera.domain.post.entity.QPublicPost.publicPost;
import static com.sparta.heartvera.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.follow.repository.FollowRepository;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import com.sparta.heartvera.domain.post.dto.PostRequestDto;
import com.sparta.heartvera.domain.post.dto.PublicPostResponseDto;
import com.sparta.heartvera.domain.post.entity.PublicPost;
import com.sparta.heartvera.domain.post.repository.PublicPostRepository;
import com.sparta.heartvera.domain.user.entity.User;
import com.sparta.heartvera.domain.user.service.UserService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublicPostService {

  private final UserService userService;
  private final FollowRepository followRepository;
  private final PublicPostRepository publicPostRepository;
  private final LikeRepository likeRepository;

  private final JPAQueryFactory queryFactory;
  private final EntityManager entityManager;

  public PublicPostResponseDto savePost(PostRequestDto requestDto, User user) {
    PublicPost post = publicPostRepository.save(new PublicPost(requestDto, user));
    return new PublicPostResponseDto(post);
  }

  // 비익명 게시물 단건 조회
  @Transactional(readOnly = true)
  public PublicPostResponseDto getPost(Long postId) {
    PublicPost post = publicPostRepository.findByPublicPostId(postId);
    if (post == null) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }
    int likeCount = likeRepository.getLikesCount(postId, LikeEnum.PUBPOST);
    return new PublicPostResponseDto(post, likeCount);
  }

  @Transactional
  public PublicPostResponseDto editPost(Long postId, PostRequestDto requestDto, User user) {
    PublicPost post = findById(postId);
    checkUserSame(post, user);
    post.update(requestDto);
    return new PublicPostResponseDto(post);
  }

  public String deletePost(Long postId, User user) {
    PublicPost post = findById(postId);
    checkUserSame(post, user);
    publicPostRepository.delete(post);

    return postId + "번 게시물 삭제 완료";
  }

  public Object getAllPost(int page, int amount) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    Pageable pageable = PageRequest.of(page, amount, sort);
    Page<PublicPost> postList = publicPostRepository.findAll(pageable);

    if (postList.getTotalElements() == 0) {
      return "먼저 작성하여 소식을 알려보세요!";
    }

    return postList.map(PublicPostResponseDto::new);
  }

  // 좋아하는 비익명 게시글 목록 조회
  @Transactional(readOnly = true)
  public List<PublicPostResponseDto> getLikePublicPosts(int page, int pageSize, Long userId) {
    List<Long> likedPostIds = likeRepository.getLikedPublicPostIds(userId);
    List<PublicPost> publicPostList = publicPostRepository.findByPublicPostId(page, pageSize, likedPostIds);

    if(publicPostList.isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_LIKE);
    }
    List<PublicPostResponseDto> publicPostResponseDtos = new ArrayList<>();
    for (PublicPost publicPost : publicPostList) {
      int likedCount = likeRepository.getLikesCount(publicPost.getId(), LikeEnum.PUBPOST);
      PublicPostResponseDto dto = new PublicPostResponseDto(publicPost, likedCount);
      publicPostResponseDtos.add(dto);
    }
    return publicPostResponseDtos;
  }

  // 팔로워한 사람들의 비익명게시물 목록 조회(생성일자,작성자명 기준 정렬)
  @Transactional(readOnly = true)
  public List<PublicPostResponseDto> getFollowedPublicPostsOrderByCreatedAt(Long userId, int page, int pageSize, String orderBy) {

    List<Long> followedUserIds = followRepository.findFollowedUserIds(userId);
    List<PublicPost> publicPostList = publicPostRepository.findByPublicPostId(page, pageSize, followedUserIds, orderBy);

    if(publicPostList.isEmpty()) {
      throw new CustomException(ErrorCode.EMPTY_FOLLOW);
    }

    List<PublicPostResponseDto> publicPostResponseDtos = new ArrayList<>();
    for(PublicPost publicPost : publicPostList) {
        int likedCount = likeRepository.getLikesCount(publicPost.getId(), LikeEnum.PUBPOST);
        PublicPostResponseDto responseDto = new PublicPostResponseDto(publicPost, likedCount);
        publicPostResponseDtos.add(responseDto);
    }

    return publicPostResponseDtos;
  }

  public PublicPost findById(Long postId) {
    return publicPostRepository.findById(postId).orElseThrow(
            () -> new CustomException(ErrorCode.POST_NOT_FOUND)
    );
  }

  private void checkUserSame(PublicPost post, User user) {
    if (!(post.getUser().getUserSeq().equals(user.getUserSeq()))) {
      throw new CustomException(ErrorCode.POST_NOT_USER);
    }
  }

  public Object getAllPostForAdmin(int page, int amount) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    Pageable pageable = PageRequest.of(page, amount, sort);
    Page<PublicPost> postList = publicPostRepository.findAll(pageable);

        if (postList.getTotalElements() == 0) {
            return "먼저 작성하여 소식을 알려보세요!";
        }

    return postList.map(PublicPostResponseDto::new);
  }

  public void delete(PublicPost post) {
    publicPostRepository.delete(post);
  }

  //좋아요 유효성 검사
  public void validatePostLike(Long userId, Long postId) {
    PublicPost post = publicPostRepository.findById(postId).orElseThrow(()->
            new CustomException(ErrorCode.POST_NOT_FOUND));
    if(post.getUser().getUserSeq().equals(userId)){
      throw new CustomException(ErrorCode.POST_SAME_USER);
    }
  }

}
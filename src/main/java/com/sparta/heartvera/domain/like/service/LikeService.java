package com.sparta.heartvera.domain.like.service;

import static com.sparta.heartvera.domain.like.entity.QLike.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.domain.comment.service.CommentService;
import com.sparta.heartvera.domain.like.entity.Like;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import com.sparta.heartvera.domain.post.service.PostService;
import com.sparta.heartvera.domain.post.service.PublicPostService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final PublicPostService publicPostService;

    // 익명 게시물별 좋아요 toggle 기능
    @Transactional
    public ResponseEntity<String> togglePostLike(Long userId, Long postId) {
        postService.validatePostLike(userId, postId);
        return toggleLike(userId, postId, LikeEnum.POST);
    }

    // 공개 게시물별 좋아요 toggle 기능
    @Transactional
    public ResponseEntity<String> togglePublicPostLike(Long userId, Long postId) {
        publicPostService.validatePostLike(userId, postId);  // PublicPostService 사용
        return toggleLike(userId, postId, LikeEnum.PUBPOST);
    }

    // 댓글별 좋아요 toggle 기능
    @Transactional
    public ResponseEntity<String> toggleCommentLike(Long userId, Long commentId) {
        commentService.validateCommentLike(userId, commentId);
        return toggleLike(userId, commentId, LikeEnum.COMMENT);
    }

    // 좋아요 토글 기능 (공통 로직)
    private ResponseEntity<String> toggleLike(Long userId, Long contentId, LikeEnum contentType) {
        Optional<Like> likeOptional = findLike(userId, contentId, contentType);
        if (likeOptional.isPresent()) {
            likeRepository.delete(likeOptional.get());
            return ResponseEntity.ok("좋아요를 취소했습니다.");
        } else {
            Like like = new Like(userId, contentId, contentType);
            likeRepository.save(like);
            return ResponseEntity.ok("좋아요를 눌렀습니다.");
        }
    }

    // 좋아요 객체 찾기
    private Optional<Like> findLike(Long userId, Long contentId, LikeEnum contentType) {
        return likeRepository.findByUserIdAndContentIdAndContentType(userId, contentId, contentType);
    }

    // 좋아요 count 조회 QueryDSL
    public int getLikesCount(Long contentId, LikeEnum contentType) {
        return (int) queryFactory
            .selectFrom(like)
            .where(like.contentId.eq(contentId).and(like.contentType.eq(contentType)))
            .fetchCount();
    }

    // 게시물, 댓글별 좋아요 수 count (Jpa)
//    public int getLikesCount(Long contentId, LikeEnum contentType) {
//        return likeRepository.countByContentIdAndContentType(contentId, contentType);
//    }

}

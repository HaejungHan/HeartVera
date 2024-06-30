package com.sparta.heartvera.domain.post.service;

import static com.sparta.heartvera.domain.post.entity.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import com.sparta.heartvera.domain.like.service.LikeService;
import com.sparta.heartvera.domain.post.dto.PostRequestDto;
import com.sparta.heartvera.domain.post.dto.PostResponseDto;
import com.sparta.heartvera.domain.post.dto.PublicPostResponseDto;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.repository.PostRepository;
import com.sparta.heartvera.domain.user.entity.User;
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
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public PostResponseDto savePost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));

        return new PostResponseDto(post);
    }

    // 익명 게시물 단건조회
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findByPostId(postId);
        int likeCount = likeRepository.getLikesCount(postId, LikeEnum.POST);
        return new PostResponseDto(post, likeCount);
    }

    @Transactional
    public PostResponseDto editPost(Long postId, PostRequestDto requestDto, User user) {
        Post post = findById(postId);
        checkUserSame(post, user);
        post.update(requestDto);

        return new PostResponseDto(post);
    }

    public String deletePost(Long postId, User user) {
        Post post = findById(postId);
        checkUserSame(post, user);
        postRepository.delete(post);

        return postId + "번 게시물 삭제 완료";
    }

    public Object getAllPost(int page, int amount) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, amount, sort);
        Page<Post> postList = postRepository.findAll(pageable);

        if (postList.getTotalElements() == 0) {
            return "먼저 작성하여 소식을 알려보세요!";
        }

        return postList.map(PostResponseDto::new);
    }

    // 좋아하는 익명 게시글 목록 조회
    public List<PostResponseDto> getLikePosts(int page, int amount, Long userId) {
        Pageable pageable = PageRequest.of(page, amount);
        List<Long> likedPostIds = likeRepository.getLikedPostIds(userId);

        List<Post> postList = queryFactory
            .selectFrom(post)
            .where(post.id.in(likedPostIds))
            .orderBy(post.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        if (postList.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_LIKE);
        }

        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : postList) {
            int likedCount = likeRepository.getLikesCount(post.getId(), LikeEnum.POST);
            PostResponseDto dto = new PostResponseDto(post, likedCount);
            postResponseDtos.add(dto);
        }
        return postResponseDtos;
    }

    // 좋아요 유효성 검사
    public void validatePostLike(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new CustomException(ErrorCode.POST_NOT_FOUND));
        if (post.getUser().getUserSeq().equals(userId)) {
            throw new CustomException(ErrorCode.POST_SAME_USER);
        }
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
    }

    private void checkUserSame(Post post, User user) {
        if (!(post.getUser().getUserSeq().equals(user.getUserSeq()))) {
            throw new CustomException(ErrorCode.POST_NOT_USER);
        }
    }

    public Object getAllPostForAdmin(int page, int amount) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, amount, sort);
        Page<Post> postList = postRepository.findAll(pageable);

        if (postList.getTotalElements() == 0) {
            return "먼저 작성하여 소식을 알려보세요!";
        }

        return postList.map(PostResponseDto::new);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

}

package com.sparta.heartvera.domain.post.controller;

import com.sparta.heartvera.domain.post.dto.PostRequestDto;
import com.sparta.heartvera.domain.post.dto.PostResponseDto;
import com.sparta.heartvera.domain.post.dto.PublicPostResponseDto;
import com.sparta.heartvera.domain.post.service.PublicPostService;
import com.sparta.heartvera.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "공개 게시판 API",description = "공개 게시판 관련된 기능을 담당하는 API 입니다.")
@RequestMapping("/api/pubposts")
public class PublicPostController {

  private final PublicPostService postService;
  private final PublicPostService publicPostService;

  @Operation(summary = "공개글 작성",description = "공개 게시글을 작성합니다.")
    @PostMapping("/")
    public ResponseEntity savePost(@Valid @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicPostService.savePost(requestDto, userDetails.getUser()));
    }

    @Operation(summary = "공개글 선택 조회",description = "선택한 공개 게시글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(publicPostService.getPost(postId));
    }

    @Operation(summary = "공개글 수정",description = "공개 게시글을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity editPost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(publicPostService.editPost(postId, requestDto, userDetails.getUser()));
    }

    @Operation(summary = "공개글 삭제",description = "공개 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(publicPostService.deletePost(postId, userDetails.getUser()));
    }

    @Operation(summary = "공개글 전체 조회",description = "공개 게시글을 전체조회합니다.(한페이지당 5개씩 조회)")
    @GetMapping("/")
    public ResponseEntity getAllPost(@RequestParam("page") int page, @RequestParam(value = "amount", defaultValue = "5") int amount) {
        return ResponseEntity.status(HttpStatus.OK).body(publicPostService.getAllPost(page - 1, amount));
    }

  @Operation(summary = "좋아요한 공개글 목록 조회",description = "내가 좋아요한 공개글을 조회합니다.(한페이지당 5개씩 조회)")
  @GetMapping("/like")
  public ResponseEntity<List<PublicPostResponseDto>> getLikePublicPosts(@RequestParam("page") int page, @RequestParam(value = "size", defaultValue = "5") int pageSize, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK).body(publicPostService.getLikePublicPosts(page - 1, pageSize, userDetails.getUser().getUserSeq()));
  }

  @Operation(summary = "팔로우한 사람들의 공개글 전체 조회(생성일자/작성자명 정렬)", description = "내가 팔로우한 사람들의 글을 전체 조회(생성일자/작성자명 정렬, 한페이지당 5개씩 조회)")
  @GetMapping("/following")
  public ResponseEntity<List<PublicPostResponseDto>> getFollowedPublicPostsOrderByCreatedAt(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("page") int page,
      @RequestParam(value = "size", defaultValue = "5") int pageSize, @RequestParam("orderby") String orderBy) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(publicPostService.getFollowedPublicPostsOrderByCreatedAt(
            userDetails.getUser().getUserSeq(), page - 1, pageSize, orderBy));
  }


}

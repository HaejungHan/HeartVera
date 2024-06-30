package com.sparta.heartvera.domain.comment.controller;

import com.sparta.heartvera.domain.comment.dto.CommentRequestDto;
import com.sparta.heartvera.domain.comment.dto.CommentResponseDto;
import com.sparta.heartvera.domain.comment.service.CommentService;
import com.sparta.heartvera.domain.post.dto.PostResponseDto;
import com.sparta.heartvera.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "댓글 API",description = "댓글과 관련된 기능을 담당하는 API 입니다.")
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 작성
  @Operation(summary = "댓글 작성",description = "댓글을 작성합니다.")
  @PostMapping("/{postId}/comments")
  public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
      @RequestBody @Valid CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentService.createComment(postId, requestDto, userDetails.getUser()));
  }

  // 좋아요한 익명 댓글 목록 조회
  @Operation(summary = "좋아요한 익명댓글 목록 조회",description = "내가 좋아요한 익명 댓글을 조회합니다.(한페이지당 5개씩 조회)")
  @GetMapping("/comments/like")
  public ResponseEntity<List<CommentResponseDto>> getLikePosts(@RequestParam("page") int page, @RequestParam(value = "amount", defaultValue = "5") int amount, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.getLikeComments(page - 1, amount, userDetails.getUser().getUserSeq()));
  }

  // 선택한 게시물의 댓글 조회
  @Operation(summary = "선택 게시물 댓글 조회",description = "선택한 게시물의 댓글을 조회합니다.")
  @GetMapping("/{postId}/comments")
  public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(postId));
  }

  // 댓글 단건 조회
  @Operation(summary = "댓글 단건 조회",description = "선택한 댓글을 조회합니다.")
  @GetMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long commentId) {
    return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(commentId));
  }

  // 댓글 수정
  @Operation(summary = "댓글 수정",description = "댓글의 내용을 수정합니다.")
  @PutMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
      @PathVariable Long commentId,
      @RequestBody @Valid CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(commentService.updateComment(postId, commentId, requestDto, userDetails.getUser()));
  }

  // 댓글 삭제
  @Operation(summary = "댓글 삭제",description = "댓글을 삭제합니다.")
  @DeleteMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<String> deleteComment(@PathVariable Long postId,
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteComment(postId, commentId, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.OK).body("댓글이 성공적으로 삭제되었습니다.");
  }

}

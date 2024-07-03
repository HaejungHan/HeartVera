package com.sparta.heartvera.domain.post.controller;

import com.sparta.heartvera.domain.post.dto.PostRequestDto;
import com.sparta.heartvera.domain.post.dto.PostResponseDto;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.service.PostService;
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
@Tag(name = "익명 게시판 API",description = "익명 게시판 관련된 기능을 담당하는 API 입니다.")
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "익명글 작성",description = "익명 게시글을 작성합니다.")
    @PostMapping("/")
    public ResponseEntity savePost(@Valid @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(requestDto, userDetails.getUser()));
    }

    @Operation(summary = "익명글 선택 조회",description = "선택한 익명 게시글을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @Operation(summary = "익명글 수정",description = "익명 게시글을 수정합니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity editPost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.editPost(postId, requestDto, userDetails.getUser()));
    }

    @Operation(summary = "익명글 삭제",description = "익명 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId, userDetails.getUser()));
    }

    @Operation(summary = "익명글 전체 조회",description = "익명 게시글을 전체조회합니다.(한페이지당 5개씩 조회)")
    @GetMapping("/")
    public ResponseEntity getAllPost(@RequestParam("page") int page, @RequestParam(value = "amount", defaultValue = "5") int amount) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPostWithComments(page - 1, amount));
    }

    @Operation(summary = "좋아요한 익명글 목록 조회",description = "내가 좋아요한 익명 게시글을 조회합니다.(한페이지당 5개씩 조회)")
    @GetMapping("/like")
    public ResponseEntity<List<PostResponseDto>> getLikePosts(@RequestParam("page") int page, @RequestParam(value = "amount", defaultValue = "5") int amount, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getLikePosts(page - 1, amount, userDetails.getUser().getUserSeq()));
    }
}

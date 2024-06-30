package com.sparta.heartvera.domain.follow.controller;

import com.sparta.heartvera.domain.follow.dto.FollowRequestDto;
import com.sparta.heartvera.domain.follow.dto.FollowResponseDto;
import com.sparta.heartvera.domain.follow.service.FollowService;
import com.sparta.heartvera.domain.user.entity.User;
import com.sparta.heartvera.domain.user.service.UserService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "팔로우 API",description = "팔로우와 관련된 기능을 담당하는 API 입니다.")
@RequestMapping("/api")
public class FollowController {

  private final UserService userService;
  private final FollowService followService;

  // 팔로우
  @Operation(summary = "팔로우",description = "팔로우 기능을 수행합니다.")
  @PostMapping("/follower")
  public ResponseEntity<String> followUser(@RequestBody @Valid FollowRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User toUser = userService.findByUserSeq(requestDto.getToUserSeq());
    User fromUser = userService.findByUserSeq(userDetails.getUser().getUserSeq());
    followService.followUser(toUser, fromUser);
    return ResponseEntity.status(HttpStatus.OK).body(toUser.getUserId() + " 님을 팔로우 하였습니다.");
  }

  // 언팔로우
  @Operation(summary = "언팔로우",description = "팔로우한 회원을 언팔로우합니다.")
  @DeleteMapping("/follower")
 public ResponseEntity<String> deleteFollowUser(@RequestBody @Valid FollowRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    User toUser = userService.findByUserSeq(requestDto.getToUserSeq());
    User fromUser = userService.findByUserSeq(userDetails.getUser().getUserSeq());
    followService.deleteFollowUser(toUser, fromUser);
    return ResponseEntity.status(HttpStatus.OK).body(toUser.getUserId() + " 님을 팔로우취소 하였습니다.");
  }

  // 내가 팔로우한 사람들 조회
  @Operation(summary = "내가 팔로우한 사람 조회",description = "본인이 팔로우한 사람을 조회합니다.")
  @GetMapping("/followers")
  public ResponseEntity<List<FollowResponseDto>> getFollowings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    User user = userService.findByUserSeq(userDetails.getUser().getUserSeq());
    List<FollowResponseDto> followers = followService.getFollowings(user);
    return ResponseEntity.status(HttpStatus.OK).body(followers);
  }
}

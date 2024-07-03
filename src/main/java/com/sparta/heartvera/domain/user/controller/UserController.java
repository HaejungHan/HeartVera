package com.sparta.heartvera.domain.user.controller;

import com.sparta.heartvera.domain.user.dto.UserPwRequestDto;
import com.sparta.heartvera.domain.user.dto.UserRequestDto;
import com.sparta.heartvera.domain.user.dto.UserResponseDto;
import com.sparta.heartvera.domain.user.service.UserService;
import com.sparta.heartvera.security.service.UserDetailsImpl;
import com.sparta.heartvera.security.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "유저 API",description = "유저와 관련된 기능을 담당하는 API 입니다.")
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자의 프로필 조회
    @Operation(summary = "사용자 프로필 조회",description = "사용자의 프로필을 조회합니다.")
    @GetMapping("/users/profile")
    public ResponseEntity<UserResponseDto> getUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(userDetails.getUser().getUserSeq()));
    }

    // 사용자의 프로필 수정
    @Operation(summary = "사용자 프로필 수정",description = "사용자의 프로필을 수정합니다.")
    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody @Valid UserRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(requestDto, userDetails.getUser().getUserSeq()));
    }

    // 비밀번호 변경
    @Operation(summary = "사용자 비밀번호 변경",description = "본인의 비밀번호를 변경합니다.")
    @PutMapping("/users/password")
    public ResponseEntity updatePassword(@RequestBody @Valid UserPwRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(requestDto, userDetails.getUser().getUserSeq());
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 변경되었습니다.");
    }

    // 로그아웃
    @Operation(summary = "사용자 로그아웃",description = "사용자가 로그아웃합니다.")
    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response) {
        // 헤더에 남아있는 정보 지우기
        request.removeAttribute(JwtUtil.AUTHORIZATION_HEADER);
        userService.logout(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃이 완료되었습니다.");
    }
}

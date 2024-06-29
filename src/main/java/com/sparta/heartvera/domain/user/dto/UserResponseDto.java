package com.sparta.heartvera.domain.user.dto;

import com.sparta.heartvera.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

  private String userId;
  private String userName;
  private String userEmail;
  private String description;
  private int likedPostCount;
  private int likedCommentCount;

  public UserResponseDto(User user) {
    this.userId = user.getUserId();
    this.userName = user.getUserName();
    this.userEmail = user.getUserEmail();
    this.description = user.getDescription();
  }

  public UserResponseDto(User user, int likedPostCount, int likedCommentCount) {
    this.userId = user.getUserId();
    this.userName = user.getUserName();
    this.userEmail = user.getUserEmail();
    this.description = user.getDescription();
    this.likedPostCount = likedPostCount;
    this.likedCommentCount = likedCommentCount;
  }
}

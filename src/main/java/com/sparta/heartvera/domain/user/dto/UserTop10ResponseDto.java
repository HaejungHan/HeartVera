package com.sparta.heartvera.domain.user.dto;

import lombok.Getter;

@Getter
public class UserTop10ResponseDto {

  private String userId;
  private String userName;
  private String userEmail;
  private String description;
  private int followCount;

  public UserTop10ResponseDto(String userId, String userName, String userEmail, String description, int followCount) {
    this.userId = userId;
    this.userName = userName;
    this.userEmail = userEmail;
    this.description = description;
    this.followCount = followCount;
  }
}

package com.sparta.heartvera.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostSearchCond {

  private String userName;

  public PostSearchCond (String userName) {
    this.userName = userName;
  }
}

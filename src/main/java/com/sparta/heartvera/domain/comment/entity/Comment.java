package com.sparta.heartvera.domain.comment.entity;

import com.sparta.heartvera.common.Timestamped;
import com.sparta.heartvera.domain.comment.dto.CommentRequestDto;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String contents;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  public Comment(CommentRequestDto requestDto, Post post, User user){
    this.contents = requestDto.getContents();
    this.post = post;
    this.user = user;
  }

  public void updateComment(CommentRequestDto requestDto){
    this.contents = requestDto.getContents();
  }
}

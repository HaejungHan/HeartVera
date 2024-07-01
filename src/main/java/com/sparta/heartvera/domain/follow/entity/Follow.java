package com.sparta.heartvera.domain.follow.entity;

import com.sparta.heartvera.common.Timestamped;
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

@Getter
@Entity
@NoArgsConstructor
public class Follow extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "follow_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "from_user")
  private User fromUser;

  @ManyToOne
  @JoinColumn(name = "to_user")
  private User toUser;

  public Follow(User fromUser, User toUser){
    this.fromUser = fromUser;
    this.toUser =toUser;
  }
}

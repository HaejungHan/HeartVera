package com.sparta.heartvera.domain.follow.repository;

import com.sparta.heartvera.domain.follow.entity.Follow;
import com.sparta.heartvera.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

  Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
  List<Follow> findByFromUser(User currentUser);

}

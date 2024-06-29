package com.sparta.heartvera.domain.like.repository;

import com.sparta.heartvera.domain.like.entity.Like;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeCustomRepository{
  // 유저 ID , contents ID, contents type으로 해당 좋아요가 있는지 확인
  Optional<Like> findByUserIdAndContentIdAndContentType(Long userId, Long contentId, LikeEnum contentType);
  // 해당 ID를 가진 CONTENTS type이 몇개 있는지 확인
  int countByContentIdAndContentType(Long contentId, LikeEnum contentType);

}

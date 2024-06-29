package com.sparta.heartvera.domain.post.repository;

import com.sparta.heartvera.domain.post.entity.PublicPost;
import com.sparta.heartvera.domain.user.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicPostRepository extends JpaRepository<PublicPost, Long>, PublicPostCustomRepository {

  Page<PublicPost> findByUser_UserSeqInOrderByCreatedAtDesc(Collection<Long> userIds, Pageable pageable);

}

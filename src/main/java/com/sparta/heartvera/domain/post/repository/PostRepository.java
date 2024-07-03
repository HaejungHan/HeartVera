package com.sparta.heartvera.domain.post.repository;

import com.sparta.heartvera.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
  @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.comments")
  Page<Post> findAllWithComments(Pageable pageable);
}

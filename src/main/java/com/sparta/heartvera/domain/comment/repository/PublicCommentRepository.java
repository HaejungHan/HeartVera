package com.sparta.heartvera.domain.comment.repository;

import com.sparta.heartvera.domain.comment.entity.PublicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicCommentRepository extends JpaRepository<PublicComment, Long> {

}

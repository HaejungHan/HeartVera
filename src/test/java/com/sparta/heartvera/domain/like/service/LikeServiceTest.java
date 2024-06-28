package com.sparta.heartvera.domain.like.service;

import com.sparta.heartvera.domain.comment.service.CommentService;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import com.sparta.heartvera.domain.post.service.PostService;
import com.sparta.heartvera.domain.post.service.PublicPostService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

  @Mock
  LikeRepository likeRepository;

  @Mock
  PostService postService;

  @Mock
  CommentService commentService;

  @Mock
  PublicPostService publicPostService;

  @InjectMocks
  LikeService likeService;



}

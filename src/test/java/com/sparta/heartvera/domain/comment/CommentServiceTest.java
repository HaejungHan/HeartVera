package com.sparta.heartvera.domain.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.comment.dto.CommentResponseDto;
import com.sparta.heartvera.domain.comment.entity.Comment;
import com.sparta.heartvera.domain.comment.repository.CommentRepository;
import com.sparta.heartvera.domain.comment.service.CommentService;
import com.sparta.heartvera.domain.like.repository.LikeRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  @Mock
  private LikeRepository likeRepository;

  @Mock
  private CommentRepository commentRepository;

  @InjectMocks
  private CommentService commentService;

  @Test
  @DisplayName("좋아요한 댓글아이디 리스트 가져오기 성공테스트")
  void getLikedCommentIds() {
    // given
    Long userId = 1L;
    List<Long> likedCommentIds = Arrays.asList(1L, 2L, 3L);
    when(likeRepository.getLikedCommentIds(userId)).thenReturn(likedCommentIds);

    // when
    List<Long> likedCommentIdsResult = likeRepository.getLikedCommentIds(userId);

    // then
    assertEquals(likedCommentIds.size(), likedCommentIdsResult.size());
  }

  @Test
  @DisplayName("좋아요한 댓글아이디가 없을 경우 실패테스트")
  void isEmptyLikedCommentIds() {
    // given
    Long userId = 1L;
    when(likeRepository.getLikedCommentIds(userId)).thenReturn(Collections.emptyList());

    // when
    CustomException customException = assertThrows(CustomException.class, () -> commentService.getLikeComments(1, 5, userId));

    // then
    assertEquals(ErrorCode.EMPTY_LIKE, customException.getErrorCode());
  }

  @Test
  @DisplayName("좋아요한 댓글리스트 아이디로 댓글가져오기")
  void findComments() {
    // given
    int page = 1;
    int amount = 5;
    Comment comment1 = Mockito.mock(Comment.class);
    Comment comment2 = Mockito.mock(Comment.class);
    Comment comment3 = Mockito.mock(Comment.class);
    when(comment1.getContents()).thenReturn("댓글내용1");
    when(comment2.getContents()).thenReturn("댓글내용2");
    when(comment3.getContents()).thenReturn("댓글내용3");

    Long userId = 1L;
    List<Long> likedCommentIds = Arrays.asList(1L, 2L, 3L);
    when(likeRepository.getLikedCommentIds(userId)).thenReturn(likedCommentIds);
    List<Comment> comments = Arrays.asList(comment1, comment2, comment3);
    when(commentRepository.findComments(page, amount, likedCommentIds)).thenReturn(comments);

    // when
    List<CommentResponseDto> commentListResult = commentService.getLikeComments(page, amount, userId);

    // then
    assertEquals(comments.size(), commentListResult.size());
  }
}

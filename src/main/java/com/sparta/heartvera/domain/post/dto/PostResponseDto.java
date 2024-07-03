package com.sparta.heartvera.domain.post.dto;

import com.sparta.heartvera.domain.comment.dto.CommentResponseDto;
import com.sparta.heartvera.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PostResponseDto {
    String title;
    String content;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    int likeCount;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post, int likeCount) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCount = likeCount;
    }

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.comments = post.getComments().stream().map(CommentResponseDto::new).toList();
    }
}

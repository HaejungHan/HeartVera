package com.sparta.heartvera.domain.post.dto;

import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.entity.PublicPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PublicPostResponseDto {
    String title;
    String content;
    String userName;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;
    int likeCount;

    public PublicPostResponseDto(PublicPost post, int likeCount) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userName = post.getUser().getUserName();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCount = likeCount;
    }

    public PublicPostResponseDto(PublicPost post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userName = post.getUser().getUserName();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.likeCount = 0;
    }

    public PublicPostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userName = post.getUser().getUserName();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}

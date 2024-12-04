package com.yubin.SpringBootTest.service;

import com.yubin.SpringBootTest.model.Comment;
import com.yubin.SpringBootTest.model.Post;
import com.yubin.SpringBootTest.model.User;
import com.yubin.SpringBootTest.repository.CommentRepository;

import com.yubin.SpringBootTest.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 게시글 ID로 댓글 생성
    public Comment createComment(Long postId, Comment comment, User user) {
        // 게시글을 postId로 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

}

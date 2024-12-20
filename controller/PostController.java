package com.yubin.SpringBootTest.controller;

import com.yubin.SpringBootTest.model.User;
import com.yubin.SpringBootTest.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.yubin.SpringBootTest.model.Comment;
import com.yubin.SpringBootTest.model.Post;
import com.yubin.SpringBootTest.service.CommentService;
import com.yubin.SpringBootTest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // 모든 게시글 조회
    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        // 날짜 형식화
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Post post : posts) {
            String formattedDate = post.getCreatedAt().format(formatter);
            post.setFormattedDate(formattedDate);
        }
        model.addAttribute("posts", posts);
        return "post";
    }


    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            Post postEntity = post.get();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");
            String formattedPostDate = postEntity.getCreatedAt().format(formatter);

            postEntity.setFormattedDate(formattedPostDate);

            List<Comment> comments = commentService.getCommentsByPostId(id);
            for (Comment comment : comments) {
                String formattedCommentDate = comment.getCreatedAt().format(formatter);
                comment.setFormattedDate(formattedCommentDate);
            }

            model.addAttribute("post", postEntity);
            model.addAttribute("comments", comments);

            return "postDetail"; // postDetail.html로 이동
        } else {
            return "404";
        }
    }




    // 새로운 게시글 작성 페이지 렌더링
    @GetMapping("/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("post", new Post()); // 빈 Post 객체 전달
        return "newPost"; // newPost.html 렌더링
    }

    // 게시글 생성
    @PostMapping
    public String createPost(Post post) {
        // 로그인한 사용자의 ID 가져오기
        Long userId = userService.getLoggedInUserId();

        // 로그인한 사용자의 User 객체 가져오기
        User user = userService.findById(userId);

        // 게시글에 사용자 할당
        post.setUser(user);  // User 객체를 할당

        postService.createPost(post);
        return "redirect:/post";  // 게시글 목록으로 리디렉션
    }

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable Long postId, @ModelAttribute Comment comment) {
        // 현재 로그인한 사용자 가져오기
        Long userId = userService.getLoggedInUserId(); // 로그인된 사용자 ID 가져오기
        User user = userService.findById(userId); // User 객체 조회

        // 댓글 작성
        commentService.createComment(postId, comment, user);

        // 작성 후 해당 게시글 상세 페이지로 리디렉션
        return "redirect:/post/" + postId;
    }

}

package com.example.myfirstproject.entity;

import com.example.myfirstproject.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.example.myfirstproject.entity.UserRole.ADMIN;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                      // 1 대 다수
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String nickname;

    @Column
    private String body;

    public static Comment createComment(CommentDto dto, Article article, User user) {
        //예외 발생
        if(dto.getId()!=null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        if(dto.getArticleId()!=article.getId())
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다.");
        if(dto.getUserId()==null)
            throw new IllegalArgumentException(("댓글 생성 실패! 로그인 id를 입력해야 합니다"));
        //엔티티 생성 및 반환
        return new Comment(
                dto.getId(),
                article,
                user,
                dto.getNickname(),
                dto.getBody()
        );
    }

    public void patch(CommentDto dto, User user, Comment target) {
        //예외 발생
        if(this.id!=dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력됐습니다.");
        if(user.getId()==null)
            throw new IllegalArgumentException("댓글 수정 실패! 로그인해야 합니다.");
        if(target.getUser().getId()!=user.getId() && user.getRole()!= ADMIN)
            throw new IllegalArgumentException("댓글 수정 실패! 해당 댓글을 쓴 유저가 아니거나 admin이 아닙니다.");
        //객체 갱신
        if(dto.getNickname()!=null)
            this.nickname = dto.getNickname();
        if(dto.getBody()!=null)
            this.body = dto.getBody();
    }
}

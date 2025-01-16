package com.example.myfirstproject.dto;

import com.example.myfirstproject.entity.Article;
import com.example.myfirstproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    //전송받은 제목과 내용을 필드에 저장하는 생성자 추가 --> @AllArgsConstructor

    //데이터를 잘 받았는지 확인할 toString() 메서드 추가 --> @ToString

    //폼 데이터를 받은 DTO객체를 엔티티로 반환
    public Article toEntity() {
        User user = new User();
        user.setId(userId); // User 객체에 userId 설정

        return new Article(id, user, title, content);
    }
}

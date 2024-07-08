package com.example.myfirstproject.dto;

import com.example.myfirstproject.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class NoticeForm {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드

    public Notice toEntity() {

        return new Notice(id,title,content);
    }
}

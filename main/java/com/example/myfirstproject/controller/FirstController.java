package com.example.myfirstproject.controller;

import com.example.myfirstproject.entity.Article;
import com.example.myfirstproject.entity.Notice;
import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.repository.ArticleRepository;
import com.example.myfirstproject.repository.NoticeRepository;
import com.example.myfirstproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FirstController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/hi")
    public String niceToMeetYou(Model model){
        model.addAttribute("username", "LTM");

        return "greetings";
    }

    @GetMapping("/")
    public String dir(){
        return "redirect:/hi";
    }

    @GetMapping("/home")
    public String home(Model model, @SessionAttribute(name = "userId", required = false) Long userId){
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        // 1. 모든 공지사항 데이터 가져오기
        List<Notice> noticeEntityList = (List<Notice>) noticeRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("noticeList", noticeEntityList);

        // 1. 모든 게시글 데이터 가져오기
        ArrayList<Article> articleEntityList=articleRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. 뷰 페이지 설정하기
        return "home";
    }


    @GetMapping("/bye")
    public String seeYouNext(Model model){
        model.addAttribute("nickname", "임태민");

        return "goodbye";
    }

}

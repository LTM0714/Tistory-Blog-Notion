package com.example.myfirstproject.controller;

import com.example.myfirstproject.dto.ArticleForm;
import com.example.myfirstproject.dto.CommentDto;
import com.example.myfirstproject.entity.Article;
import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.entity.UserRole;
import com.example.myfirstproject.repository.ArticleRepository;
import com.example.myfirstproject.service.CommentService;
import com.example.myfirstproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping("/articles/new")
    public String newArticleForm(Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                                 RedirectAttributes rttr){
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
            model.addAttribute("user_id", loginUser.getId());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){
        log.info(form.toString());
        //System.out.println(form.toString());
        // 1. DTO를 엔티티로 변환
        Article article=form.toEntity();
        log.info(article.toString());
        //System.out.println(article.toString());
        // 2. 레파지터리로 엔티티를 DB에 저장
        Article saved=articleRepository.save(article);
        log.info(saved.toString());
        //System.out.println(saved.toString());
        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model, @SessionAttribute(name = "userId", required = false) Long userId){
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        log.info("id = " + id);
        // 1. id를 조회해 데이터 가져오기
        Article articleEntity=articleRepository.findById(id).orElse(null);
        List<CommentDto> commentsDtos = commentService.comments(id);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("user", loginUser);
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentsDtos);
        // 3. 뷰 페이지 반환하기
        return "articles/show";
    }

    @GetMapping("testForward")
    public String forwardTestMethod() {
        log.info("Forward test 메소드 호출");
        return "forward:/bye";
    }

    @GetMapping("/articles")
    public String index(Model model, @SessionAttribute(name = "userId", required = false) Long userId){
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        // 1. 오든 데이터 가져오기
        ArrayList<Article> articleEntityList=articleRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. 뷰 페이지 설정하기
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                       RedirectAttributes rttr){
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN) && !loginUser.getId().equals(articleRepository.findById(id).get().getUser().getId())){
                rttr.addFlashAttribute("msg", "관리자 등급이 아니거나 해당 글의 작성자가 아닙니다");
                return "redirect:/articles/" + id;
            }
            //admin과 해당 글 작성자라면 게시글 수정, 리턴 x
            model.addAttribute("nickname", loginUser.getNickname());
            model.addAttribute("user_id", loginUser.getId());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        //수정할 데이터 가져오기
        Article articleEntity=articleRepository.findById(id).orElse(null);
        //모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        // 뷰 페이지 설정하기
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());
        // 1. DTO를 엔티티로 변환하기
        Article articleEntity=form.toEntity();
        log.info(articleEntity.toString());
        // 2. 엔티티를 DB에 저장하기
        // 2-1. DB에서 기존 데이터 가져오기
        Article target=articleRepository.findById(articleEntity.getId()).orElse(null);
        // 2-2. 기존 데이터 값을 갱신하기
        if(target!=null){
            articleRepository.save(articleEntity);
        }
        // 3. 수정 결과를 페이지로 리다이렉트하기
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr,
                         Model model, @SessionAttribute(name = "userId", required = false) Long userId){
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN) && !loginUser.getId().equals(articleRepository.findById(id).get().getUser().getId())){
                rttr.addFlashAttribute("msg", "관리자 등급이 아니거나 해당 글의 작성자가 아닙니다");
                return "redirect:/articles/" + id;
            }
            //admin과 해당 글 작성자라면 게시글 삭제, 리턴 x
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        log.info("삭제 요청이 들어왔습니다!!");
        // 1. 삭제할 대상 가져오기
        Article target=articleRepository.findById(id).orElse(null);
        log.info(target.toString());
        // 2. 대상 엔티티 삭제하기
        if(target!=null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제되었습니다!");
        }
        // 3. 결과 페이지로 리다이렉트하기
        return "redirect:/articles";
    }
}

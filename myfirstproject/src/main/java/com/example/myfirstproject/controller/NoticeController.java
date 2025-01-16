package com.example.myfirstproject.controller;

import com.example.myfirstproject.dto.NoticeForm;
import com.example.myfirstproject.entity.Notice;
import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.entity.UserRole;
import com.example.myfirstproject.repository.NoticeRepository;
import com.example.myfirstproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class NoticeController {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/notices/new")
    public String newNoticeForm(Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                                RedirectAttributes rttr) {
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN)){
                rttr.addFlashAttribute("msg", "관리자 등급이 아닙니다");
                return "redirect:/session-login";
            }
            //admin이면 공지사항 쓰기, 리턴 x
            model.addAttribute("nickname", loginUser.getNickname());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        return "notices/new";
    }

    @PostMapping("/notices/create")
    public String createNotice(NoticeForm form) {  //DTO ArticleForm

        //System.out.println(form.toString());
        log.info(form.toString());

        // 1. DTO를 엔티티로 변환
        Notice notice = form.toEntity();
        log.info(notice.toString());
        //System.out.println(notice.toString());

        Notice saved = noticeRepository.save(notice);
        //System.out.println(saved.toString());
        log.info(saved.toString());
        return "redirect:/notices/"+saved.getId();
    }
    @GetMapping("/notices")
    public String index(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        List<Notice> noticeEntityList = (List<Notice>) noticeRepository.findAll();
        model.addAttribute("noticeList", noticeEntityList);
        return "notices/index";
    }

    @GetMapping("/notices/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                       RedirectAttributes rttr) {
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN)){
                rttr.addFlashAttribute("msg", "관리자 등급이 아닙니다");
                return "redirect:/notices/" + id;
            }
            //admin이면 수정하기, 리턴 x
            model.addAttribute("nickname", loginUser.getNickname());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        Notice noticeEntity = noticeRepository.findById(id).orElse(null);
        model.addAttribute("notice", noticeEntity);
        return "notices/edit";
    }
    @PostMapping("/notices/update")
    public String update(@RequestParam Long id, NoticeForm form) {
        Notice noticeEntity = form.toEntity();
        Notice target = noticeRepository.findById(noticeEntity.getId()).orElse(null);
        if (target != null) {
            noticeRepository.save(noticeEntity);
        }
        return "redirect:/notices/" + noticeEntity.getId();
    }

    @GetMapping("/notices/{id}")
    public String show(@PathVariable("id") Long id, Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        log.info("id = " + id);
        //아래의 방법으로 해도 되지만, 해당 id가 없음 에러이기때문에,
        //Optional클래스의 .orElse(null) 메소드를 쓰면 null리턴해주고 값이 있음 해당 타입클래스로 자동 리턴
        //Optional<Notice> noticeEntity = noticeRepository.findById(id);
        //Notice noticeEntity = noticeRepository.findById(id).orElse(null);
        Notice noticeEntity = noticeRepository.findById(id).orElse(null);
        model.addAttribute("notice", noticeEntity);
        return "notices/show";
    }

    @GetMapping("/notices/{id}/delete")
    public String delete(@PathVariable Long id, @SessionAttribute(name = "userId", required = false) Long userId,
                         RedirectAttributes rttr, Model model) {
        User loginUser = userService.getLoginUserById(userId);

        Notice target = noticeRepository.findById(id).orElse(null);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN)){
                rttr.addFlashAttribute("msg", "관리자 등급이 아닙니다");
                return "redirect:/notices/" + id;
            }
            //admin이면 삭제하기
            if (target != null) {
                noticeRepository.delete(target);
                rttr.addFlashAttribute("msg", "삭제됐습니다!");
            }
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }


        return "redirect:/notices";
    }

}

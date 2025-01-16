package com.example.myfirstproject.controller;

import com.example.myfirstproject.dto.UserForm;
import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.entity.UserRole;
import com.example.myfirstproject.repository.UserRepository;
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

import java.util.List;

@Slf4j
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public String show(@PathVariable Long id, Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                       RedirectAttributes rttr){
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN)){
                rttr.addFlashAttribute("msg", "관리자 등급이 아닙니다");
                return "redirect:/session-login";
            }
            //admin이면 회원세부사항 보기, 리턴 x
            model.addAttribute("nickname", loginUser.getNickname());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        //1. id를 조회해 데이터 가져오기
        User userEntity= userRepository.findById(id).orElse(null);
        //2. 모델에 데이터 등록하기
        model.addAttribute("member", userEntity);
        //3. 뷰 페이지 반환하기
        return "users/show";
    }

    @GetMapping("/users")
    public String index(Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                        RedirectAttributes rttr){
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN)){
                rttr.addFlashAttribute("msg", "관리자 등급이 아닙니다");
                return "redirect:/session-login";
            }
            //admin이면 회원목록 보기, 리턴 x
            model.addAttribute("nickname", loginUser.getNickname());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        //1. 모든 데이터 가져오기
        List<User> memberEntityList=(List<User>) userRepository.findAll();
        //2. 모델에 데이터 등록하기
        model.addAttribute("memberList", memberEntityList);
        //3. 뷰 페이지 설정하기
        return "users/index";
    }

    @GetMapping("/users/{id}/edit")
    public String edit(@PathVariable Long id, Model model, @SessionAttribute(name = "userId", required = false) Long userId,
                       RedirectAttributes rttr){
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser != null) {
            if(!loginUser.getRole().equals(UserRole.ADMIN)){
                rttr.addFlashAttribute("msg", "관리자 등급이 아닙니다");
                return "redirect:/session-login";
            }
            //admin이면 회원목록 보기, 리턴 x
            model.addAttribute("nickname", loginUser.getNickname());
        } else {
            rttr.addFlashAttribute("msg", "로그인을 해야 합니다");
            return "redirect:/session-login";
        }

        // 수정할 데이터 가져오기
        User memberEntity= userRepository.findById(id).orElse(null);
        // 모델에 데이터 등록하기
        model.addAttribute("member", memberEntity);
        // 뷰 페이지 설정하기
        return "users/edit";
    }

    @PostMapping("/users/update")
    public String update(UserForm form){
        log.info(form.toString());
        //1. DTO를 엔티티로 변환하기
        User memberEntity=form.toEntity();
        log.info(memberEntity.toString());
        //2. 엔티티를 DB에 저장하기
        //2-1. DB에서 기존 데이터 가져오기
        User target= userRepository.findById(memberEntity.getId()).orElse(null);
        //2-2. 기존 데이터 값을 갱신하기
        if(target!=null){
            userRepository.save(memberEntity);
        }
        //3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/users/" + memberEntity.getId();
    }

    @GetMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!!");
        //1. 삭제할 대상 가져오기
        User target= userRepository.findById(id).orElse(null);
        log.info(target.toString());
        //2. 대상 엔티티 삭제하기
        if(target!=null){
            userRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        //3. 결과 페이지로 리다이렉트하기
        return "redirect:/users";
    }
}

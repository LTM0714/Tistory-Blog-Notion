package com.example.myfirstproject.controller;

import com.example.myfirstproject.dto.JoinRequest;
import com.example.myfirstproject.dto.LoginRequest;
import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.entity.UserRole;
import com.example.myfirstproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Controller
@RequestMapping("/session-login")
public class SessionLoginController {
    @Autowired
    private UserService userService;

    //홈
    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);
        if(loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }
        return "sessionLogin/home";
    }

    //회원가입
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "sessionLogin/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute JoinRequest joinRequest) {
        userService.join(joinRequest);
        return "sessionLogin/home";
    }

    //로그인
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "sessionLogin/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,     //폼 데이터 바인딩 및 유효성 검사 결과
                        HttpServletRequest httpServletRequest) {        //세션 관리, 요청 정보 접근, 요청 속성 설정 등 다양한 HTTP 관련 작업
        User user = userService.login(loginRequest);

        if(user == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if(bindingResult.hasErrors()) {
            return "sessionLogin/login";
        }

        httpServletRequest.getSession().invalidate();                       // 기존 세션을 무효화
        HttpSession session = httpServletRequest.getSession(true);      // 새로운 세션을 생성합니다

        session.setAttribute("userId", user.getId());   // 세션에 새로운 ID를 저장
        session.setMaxInactiveInterval(1800);               // 유효 기간을 1800초 설정

        sessionList.put(session.getId(), session);          // 해시 테이블에 세션ID와 세션 객체 저장

        return "redirect:/session-login";
    }

    public static Hashtable sessionList = new Hashtable();

    @GetMapping("/session-list")
    @ResponseBody
    public Map<String, String> sessionList() {
        Enumeration elements = sessionList.elements();      // sesssionList 해시테이블의 모든 요소를 열겨형으로 가져옴
        Map<String, String> lists = new HashMap<>();        // 세션ID, 사용자ID를 저장할 새로운 맵 생성
        while(elements.hasMoreElements()) {
            HttpSession session = (HttpSession)elements.nextElement();  // HttpSession 객체로 변환
            lists.put(session.getId(), String.valueOf(session.getAttribute("userId")));
        }
        return lists;
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // Session이 없으면 null return
        if(session != null) {
            sessionList.remove(session.getId());
            session.invalidate();
        }
        return "redirect:/session-login/";
    }

    //유저 정보
    @GetMapping("/info")
    public String userInfo(@SessionAttribute(name = "userId", required = false) Long userId, Model model) {

        User loginUser = userService.getLoginUserById(userId);

        if(loginUser == null) {
            return "redirect:/session-login/login";
        } else {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        model.addAttribute("user", loginUser);

        return "sessionLogin/info";
    }

    //관리자 창
    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "userId", required = false) Long userId, Model model) {
        User loginUser = userService.getLoginUserById(userId);

        if(loginUser == null) {
            return "redirect:/session-login/login";
        } else {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        if(!loginUser.getRole().equals(UserRole.ADMIN)) {       // admin이 아니면 -> ! false -> 홈으로
            System.out.println("관리자");
            return "redirect:/session-login";
        }

        return "sessionLogin/admin";
    }

}

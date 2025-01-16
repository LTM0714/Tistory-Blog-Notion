package com.example.myfirstproject.controller;

import com.example.myfirstproject.dto.MemberForm;
import com.example.myfirstproject.entity.Member;
import com.example.myfirstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/signup")
    public String newMemberForm(){
        return "/members/new";
    }

    @PostMapping("/join")
    public String createMember(MemberForm form){
        log.info(form.toString());
        // 1. DTO를 엔티티로 변환
        Member member=form.toEntity();
        log.info(member.toString());
        // 2. 레파지터리로 엔티티를 DB에 저장
        Member saved = memberRepository.save(member);
        log.info(saved.toString());

        return "redirect:/members/" + saved.getId();
    }

    @GetMapping("/members/{id}")
    public String show(@PathVariable Long id, Model model){
        //1. id를 조회해 데이터 가져오기
        Member memberEntity=memberRepository.findById(id).orElse(null);
        //2. 모델에 데이터 등록하기
        model.addAttribute("member", memberEntity);
        //3. 뷰 페이지 반환하기
        return "members/show";
    }

    @GetMapping("/members")
    public String index(Model model){
        //1. 모든 데이터 가져오기
        List<Member> memberEntityList=(List<Member>) memberRepository.findAll();
        //2. 모델에 데이터 등록하기
        model.addAttribute("memberList", memberEntityList);
        //3. 뷰 페이지 설정하기
        return "members/index";
    }

    @GetMapping("/members/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터 가져오기
        Member memberEntity=memberRepository.findById(id).orElse(null);
        // 모델에 데이터 등록하기
        model.addAttribute("member", memberEntity);
        // 뷰 페이지 설정하기
        return "members/edit";
    }

    @PostMapping("/members/update")
    public String update(MemberForm form){
        log.info(form.toString());
        //1. DTO를 엔티티로 변환하기
        Member memberEntity=form.toEntity();
        log.info(memberEntity.toString());
        //2. 엔티티를 DB에 저장하기
        //2-1. DB에서 기존 데이터 가져오기
        Member target=memberRepository.findById(memberEntity.getId()).orElse(null);
        //2-2. 기존 데이터 값을 갱신하기
        if(target!=null){
            memberRepository.save(memberEntity);
        }
        //3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/members/" + memberEntity.getId();
    }

    @GetMapping("/members/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!!");
        //1. 삭제할 대상 가져오기
        Member target=memberRepository.findById(id).orElse(null);
        log.info(target.toString());
        //2. 대상 엔티티 삭제하기
        if(target!=null){
            memberRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제됐습니다!");
        }
        //3. 결과 페이지로 리다이렉트하기
        return "redirect:/members";
    }
}

package com.example.finalapp.controller.member;

import com.example.finalapp.dto.member.MemberJoinDTO;
import com.example.finalapp.dto.member.MemberLoginDTO;
import com.example.finalapp.dto.member.MemberSessionDTO;
import com.example.finalapp.exception.member.MemberDuplicateException;
import com.example.finalapp.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public String join() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(MemberJoinDTO memberJoinDTO) {
        log.debug("memberJoinDTO: {}", memberJoinDTO);
        try{
            memberService.addMember(memberJoinDTO);
            return "redirect:/member/login";
        } catch (MemberDuplicateException e) {
            log.error(e.getMessage());
            return "redirect:/member/join";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(defaultValue = "false") boolean hasError,
                        Model model) {
        model.addAttribute("hasError", hasError);

        return "member/login";
    }

    @PostMapping("/login")
    public String login(MemberLoginDTO memberLoginDTO,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // RedirectAttributes rediret 전용 모델
        try {
            MemberSessionDTO loginInfo = memberService.getLoginInfo(memberLoginDTO);
            session.setAttribute("memberId", loginInfo.getMemberId());
            session.setAttribute("loginId", loginInfo.getLoginId());
            session.setAttribute("role", loginInfo.getRole());

            return "redirect:/";
        } catch (Exception e) {
            log.error(e.getMessage());

            // redirectAttributes는 스프링 MVC에서 제공하는 객체이다.
            // 리디렉션을 할 때 데이터를 전달할 수 있는 방법을 제공한다.

            // addAttribute()를 사용하면 리디렉션하는 URL에 쿼리스트링을 활용하여 데이터를 전달한다.
            // 쿼리스트링은 핸들러 메서드에서 사용하는 데이터이다.
            // 즉, addAttribute()는 리디렉션 되는 핸들러 메서드에게 데이터를 전달하는 것!
            // 컨트롤러에 전달
            redirectAttributes.addAttribute("hasError", true);

            // addFlashAttribute()를 사용하면 쿼리스트링이 아닌 세션에 데이터를 숨겨 전달한다.
            // 해당 데이터는 리디렉션되는 핸들러 메서드에서 자동으로 Model에 담기게 된다.
            // Model에 데이터를 저장한 후에 자동으로 세션에서 삭제됨
            // 즉, 리디렉션되는 핸들러 메서드가 포워딩하는 view에 데이터를 전달하는 것!!
            // view에 전달
            // 바로 화면에 쓸거라면 addFlashAttribute
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/member/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }

}

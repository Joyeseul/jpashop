package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /* @Valid : 필수 요소
     * BindResult : 에러 내용을 받아올 수 있다. 그리고 에러를 화면까지 끌고갈 수 있다.
     * */
    /* Form 객체 vs 엔터티 직접 사용
     * 받아올 때 Member 객체를 사용하여 받아와도 되긴한다. 그러나 form 에서 입력하는 내용과 Member 엔티티가 다를 수 있다.
     * (그리고 validation 을 하려면 Member 엔티티 필드마다 @NotEmpty 덕지덕지 달아야 하는 등 깔끔치 못한 문제도 발생)
     * 그래서 form 에 딱 맞는 MemberForm 을 따로 만들어서 사용하는게 더 깔끔하고 좋을 수 있다. (or DTO)
     * JPA 에선 Entity 는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다.
     * 그래야 기능이 늘어나더라도 여러 곳에서 유연하게 사용할 수 있다. 
     * */
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        //result 에 error 가 있다면, form 화면으로 error 내용도 끌고가서 출력한다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());//쉬운 다른 방법이 있다는데..

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    /* 여기에서도, Member Entity 를 직접 넘기지 말고, 화면에 꼭 필요한 memberDTO 를 만들어서 뿌리는 게 좋다.
    * + 단, API 를 만들 때는 무!조!건! Entity 를 외부에 반환하면 안된다.
    * Entity 에 필드를 하나만 추가하더라도, API 스펙이 달라져버리기 때문임
    * */
    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}


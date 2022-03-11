package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor        //final 이 붙은 필드만 생성자 만듦
public class MemberService {
    
    //필드 주입
//    @Autowired
//    private MemberRepository memberRepository;


    //세터 주입
//    private MemberRepository memberRepository;

//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


    private final MemberRepository memberRepository;

    //생성자 주입 (생성자가 하나면 @Autowired 생략 가능)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /** 
     * 회원가입
     * */
    @Transactional  //기본값은 readOnly = false
    public Long join(Member member) {
        
        validateDuplicateMember(member);    //중복 회원 검증
        memberRepository.save(member);      //영속성 컨텍스트에 persist() 되면, 그 키가 엔티티의 PK 값이다.
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //여기에 더해서 멀티 Thread 환경을 고려해, DB의 name 에 unique constraint 를 걸어주는 게 좋다.
        List<Member> findMembers = memberRepository.findByName(member.getName());

        //또는 세보고 크기가 0이상이면 exeption으로 해도 됨
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     * */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}

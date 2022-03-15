package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.*;

/* @ExtendWith,
* @DataJpaTest
* @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
* 등에 대해서도 알아보기
* */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    /* 대신에 @MockBean 으로 주입받을 수도 있다. */

    @Test
    //@Rollback(value = false)    //롤백 안 하고 commit 실행
    public void 회원가입() throws Exception {
        //Given
        /* 만약 setter 가 protected로 막혀있을 경우, 생성하는 방법을 따로 찾아야 한다. */
        Member member = new Member();
        member.setName("kim");
        
        //When
        Long saveId = memberService.join(member);
        
        //Then
        em.flush(); //DB로 나가는 insert 쿼리가 보고 싶은 경우 작성
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예약() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
//        try {
//            memberService.join(member2);        //예외가 발생해야 함
//        } catch (IllegalStateException e) {
//            return;
//        }
        memberService.join(member2);        //예외가 발생해야 함

        //then
        fail("예외가 발생해야 함");
    }

}
package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Temporal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test 
    @DisplayName("멤버 주입")
    @Transactional
    @Rollback(false)
    public void MemberRepositoryTest() throws Exception {
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat((findMember.getId())).isEqualTo(member.getId());
        Assertions.assertThat((findMember.getUsername())).isEqualTo(member.getUsername());
        Assertions.assertThat((findMember)).isEqualTo(member);
        System.out.println("findMember == member: " + (findMember == member)); //같은 영속성 컨텍스트 이므로 true 가 뜸
    }
}